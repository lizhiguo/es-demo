package com.qp.esdemo

import com.jillesvangurp.ktsearch.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EsDemoApplicationTests {
	val client = SearchClient(
		KtorRestClient("127.0.0.1", 9200)
	)
	@Test
	fun contextLoads2() {
		runBlocking {
			client.root().let { resp ->
				println("${resp.variantInfo.variant}: ${resp.version.number}")
			}
			/*client.clusterHealth().let { resp ->
				println(resp.clusterName + " is " + resp.status)
			}*/
		}
	}
	@Test
	fun contextLoads() {
		runBlocking {
			// creates an index with dynamic mapping turned on
			client.createIndex("my-first-index")

			// insert some document
			val json = """{"message:"hello world"}"""
			client.indexDocument("my-first-index", json)
		}

	}
	@Test
	fun `数据映射`() {
		runBlocking {
			client.createIndex("an-index") {
				settings {
					replicas=1
					shards=3

					analysis {
						filter("2_5_edgengrams") {
							type = "edge_ngram"
							// we don't directly support all params
							// of each filter, tokenizer, etc.
							// so use put to add anything that is missing
							put("min_gram", 2)
							put("max_gram", 5)
						}
						analyzer("prefix_ngrams") {
							tokenizer = "whitespace"
							filter = listOf("2_5_edgengrams")
						}
					}
				}
				mappings(dynamicEnabled = false) {
					text(TestDocument::message) {
						copyTo = listOf("catchall")
						fields {
							keyword("keyword")
							text("completion") {
								analyzer="prefix_ngrams"
							}
						}
					}
					text("catchall") {
						norms = false
					}
					number<Double>(TestDocument::number)
					objField(TestDocument::properties) {
						keyword("foo")
					}
					keyword(TestDocument::tags) {
						copyTo = listOf("catchall")
					}
				}
			}
		}
	}
}
data class TestDocument(
	val message: String,
	val number: Double,
	val properties: Map<String, String>,
	val tags: List<String>
)
