package software.jumbo.storesfinder.configuration

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.domain.PageRequest
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.util.MultiValueMap
import software.jumbo.storesfinder.domain.StoreAddress
import software.jumbo.storesfinder.domain.StoreDoc
import software.jumbo.storesfinder.domain.StoreJson
import software.jumbo.storesfinder.domain.StoreSearch
import java.io.IOException
import java.net.URI
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

/**
 * UUID generator top level function
 */
fun genUUID(): String = UUID.randomUUID().toString()

/**
 * success json response when adding store
 */
val StoreDoc.asJsonResponseOk get() = """{"id": "$id", "name": "$name"}"""

/**
 * StoreJson model to StoreDoc model Converter ext. function
 */
fun StoreJson.asDocument(): StoreDoc =
    StoreDoc(
        id = sapStoreID,
        name = addressName,
        location = GeoJsonPoint(longitude.toDouble(), latitude.toDouble()),
        locationType = locationType,
        address = StoreAddress(
            postalCode = postalCode,
            street = street,
            number = street2,
            optional = street3,
            city = city
        ),
        isCollectionPoint = collectionPoint,
        opensAt = todayOpen,
        closesAt = todayClose
    )

/**
 * Stores search helper/functions
 */
private const val RADIUS_KM = 10.0
private const val LIMIT = 5

fun StoreSearch.asGeoJsonPoint() = GeoJsonPoint(lng, lat)
fun StoreSearch.asDistance() = Distance(radiusInKm, Metrics.KILOMETERS)

fun docsLimit(items: Int = LIMIT) = PageRequest.of(0, items)
fun parseRadius(s: String?): Double = s?.toDouble() ?: RADIUS_KM
fun parseLimit(s: String?): PageRequest = docsLimit(s?.toInt() ?: LIMIT)

typealias QueryParams = MultiValueMap<String, String>

fun QueryParams.id(key: String): String? = this[key]?.firstOrNull()

/**
 * kotlin reified wrapper used in order to capture
 *  the generic type and retain it at runtime
 */
inline fun <reified T> typeReference() = object : ParameterizedTypeReference<T>() {}


/**
 * mongo/spring-data geoJsonPoint serializers used in a corner case
 * test serialization issue
 */
internal class LocationToLatLngSerializer : JsonSerializer<GeoJsonPoint>() {
    @Throws(IOException::class)
    override fun serialize(value: GeoJsonPoint, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeNumberField("latitude", value.x)
        gen.writeNumberField("longitude", value.y)
        gen.writeEndObject()
    }
}

class GeoJsonPointSerializer : JsonSerializer<GeoJsonPoint>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: GeoJsonPoint, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeStringField("type", value.type)
        gen.writeArrayFieldStart("coordinates")
        gen.writeObject(value.coordinates)
        gen.writeEndArray()
        gen.writeEndObject()
    }
}
