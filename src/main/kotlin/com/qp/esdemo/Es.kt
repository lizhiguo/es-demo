package com.qp.esdemo

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository
import java.io.Serializable

@Document(indexName = "goods")
class Goods(
    @Id
    var id: Long = 0,
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    val title: String,
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    val remake: String,
    @Field(index=false, type = FieldType.Keyword)
    val images: String
):Serializable

@Repository
interface GoodsRepository : ElasticsearchRepository<Goods, Long> {
}