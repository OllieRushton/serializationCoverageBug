package serializationCoverageBug

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.Serializable

@Serializable
data class MySerializable(val a: String, val b: Int)

fun main() {
    val jsonString = Json.encodeToString((0..2).map { MySerializable("String: $it", it) })
    println("jsonString: $jsonString")
}
