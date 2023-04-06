package com.qp.esdemo

import com.jillesvangurp.ktsearch.*
import com.jillesvangurp.searchdsls.querydsl.bool
import com.jillesvangurp.searchdsls.querydsl.match
import com.jillesvangurp.searchdsls.querydsl.term
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate

@SpringBootTest
class EsBaseTest() {
    @Autowired
    private lateinit var elasticsearchTemplate: ElasticsearchTemplate
    @Autowired
    private lateinit var goodsRepository: GoodsRepository
    @Autowired
    private lateinit var goodsService: GoodsService
    val client = SearchClient(
        KtorRestClient("127.0.0.1", 9200)
    )
    @Test
    fun `创建索引`(){
        // 创建索引，会根据Item类的@Document注解信息来创建
//        elasticsearchTemplate.i
        // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
//        elasticsearchTemplate.putMapping(Item.class);
        for (i in 1..10000) {
            println(goodsRepository.save(Goods(id = i.toLong(), title = "我是一个好人${i}", remake = "中国人民站起来了${i}", images = "http://baidu.com/a.jpg")))
        }
//        println(goodsRepository.save(Goods(title = "我是一个好人", remake = "中国人民站起来了", images = "http://baidu.com/a.jpg")))

    }

    @Test
    fun `数据查询_直接json_and条件`() {
        val q1 = "中国"
        val q2 = "站起"
        val q3 = "好人"
        runBlocking{
            client.search(
                "goods", rawJson = """
                    {
                      "query": {
                        "bool": {
                          "must": [
                            {
                              "match": {
                                "remake": {
                                  "query": "${q1}"
                                }
                              }
                            },{
                              "match": {
                                "remake": {
                                  "query": "${q2}"
                                }
                              }
                            },{
                              "match": {
                                "title": {
                                  "query": "${q3}"
                                }
                              }
                            }
                          ]
                        }
                      }
                    }
                """.trimIndent()
            ).let {
                println(it)
            }
        }
    }
    @Test
    fun `数据查询_直接json_or条件`() {
        runBlocking{
            client.search(
                "goods", rawJson = """
                    {
                      "query": {
                        "match": {
                          "title": {
                            "query": "好人 中国人"
                          }
                        }
                      }
                    }
                """.trimIndent()
            )
        }
    }
    @Test
    fun `数据查询_SearchDSL`() {
        runBlocking{
            client.search("goods") {
                query = term(Goods::remake, "中国人")
            }.let {
                println(it)
            }
        }
    }
    @Test
    fun `数据查询_SearchDSL_api_and`(){
//        val q1 = "中国"
//        val q2 = "起来"
        val q1 = null
        val q2 = null
        runBlocking{
            val resp = client.search("goods") {
                from = 0
                resultSize = 100
                trackTotalHits = "true"
                query = bool {
                    q1?.let {
                        filter(
                            term(Goods::remake, "${q1}")
                        )
                    }
                    q2?.let {
                        filter(
                            term(Goods::remake, "${q2}")
                        )
                    }
                }

            }
            println(resp)
        }
    }
    @Test
    fun `数据查询_SearchDSL_文本查询_or`(){
        runBlocking {
            client.search("goods") {
                query = match(Goods::remake, "中国人 好人"){
                    boost = 1.5
                    lenient = true
                    autoGenerateSynonymsPhraseQuery = true
                }
            }.let {
                println(it)
            }
        }
    }
    @Test
    fun `数据查询_SearchDSL_文本查询_结果处理`(){
        val q1 = "中国"
        val q2 = "起来"
        runBlocking{
            val resp = client.search("goods") {
                from = 9700
                resultSize = 150
                trackTotalHits = "true"
                query = bool {
                    q1?.let {
                        filter(
                            term(Goods::remake, "${q1}")
                        )
                    }
                    q2?.let {
                        filter(
                            term(Goods::remake, "${q2}")
                        )
                    }
                }
            }
            println(resp)
            val totla = resp.total
            val rs = resp.parseHits<Goods>().map { it }
            println("总数为${totla}")
            println(rs.size)
            println(rs)
        }
    }
    @Test
    fun `数据查询_SearchDSL_sevrices`(){
        runBlocking{
            val gs =goodsService.search(Goods(title = "中国", remake = "人民"))
            println(gs)
        }
    }
}
