package com.qp.esdemo

import com.jillesvangurp.ktsearch.SearchClient
import com.jillesvangurp.ktsearch.parseHits
import com.jillesvangurp.ktsearch.search
import com.jillesvangurp.searchdsls.querydsl.bool
import com.jillesvangurp.searchdsls.querydsl.term
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Serializable
@Document(indexName = "goods")
data class Goods(
    @Id
    var id: Long = 0,
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    val title: String? = null,
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    val remake: String? = null,
    @Field(index=false, type = FieldType.Keyword)
    val images: String? = null
):java.io.Serializable

@Repository
interface GoodsRepository : ElasticsearchRepository<Goods, Long> {
}

@RestController
@RequestMapping("/goods")
class GoodsController(val goodsService: GoodsService) {
    @GetMapping
    suspend fun search(goods: Goods): List<Goods>? {
        return goodsService.search(goods)
    }
}

@Service
class GoodsService(val client: SearchClient) {
    suspend fun search(goods: Goods): List<Goods>? {
        val resp = client.search("goods") {
            from = 0
            resultSize = 10
            trackTotalHits = "true"
            query = bool {
                goods.title?.let {
                    filter(
                        term(Goods::title, it)
                    )
                }
                goods.remake?.let {
                    filter(
                        term(Goods::remake, it)
                    )
                }
            }
        }
        println(resp)
        return resp.parseHits<Goods>().map { it }
    }
}