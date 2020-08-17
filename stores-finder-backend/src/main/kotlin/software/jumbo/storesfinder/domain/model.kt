package software.jumbo.storesfinder.domain

import com.fasterxml.jackson.annotation.JsonRootName
import org.springframework.data.annotation.Id
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType.GEO_2DSPHERE
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.mapping.Document
import software.jumbo.storesfinder.configuration.*

/**
 *  store Representation in original provided scheme
 */
data class StoreJson(
    val uuid: String,
    val sapStoreID: String,
    val complexNumber: String,
    val longitude: String,
    val latitude: String,
    val locationType: String,
    val postalCode: String,
    val addressName: String,
    val street: String,
    val street2: String,
    val street3: String = "",
    val city: String,
    val collectionPoint: Boolean,
    val todayClose: String,
    val todayOpen: String,
    val showWarningMessage: Boolean
)


/**
 * StoreDoc model is the represent the
 * store de persisted document model.
 *  additional wrappers:  StoreAddress, ListStores
 */
@Document
class StoreDoc(
    @Id val id: String = genUUID(),
    val name: String,
    @GeoSpatialIndexed(name = "location", type = GEO_2DSPHERE) val location: GeoJsonPoint,
    val locationType: String,
    val address: StoreAddress,
    val isCollectionPoint: Boolean,
    val opensAt: String,
    val closesAt: String
)

data class StoreAddress(val postalCode: String,
                        val street: String,
                        val number: String,
                        val optional: String = "",
                        val city: String)

@JsonRootName("stores")
class ListStores(vararg val stores: StoreJson)

/**
 * Store Search request model
 * overloaded constructors for data conversion
 * longitude is the X axis, latitude is the Y axis,
 */
data class StoreSearch(val lng: Double, val lat: Double, val radiusInKm: Double, val limit: PageRequest) {
    constructor(coord: Pair<String, String>, params: QueryParams? = null)
        : this(lng = coord.second, lat = coord.first, params = params)

    constructor(lng: String, lat: String, params: QueryParams? = null)
        : this(
        lng = lng.toDouble(),
        lat = lat.toDouble(),
        radiusInKm = parseRadius(params?.id("radius")),
        limit = parseLimit(params?.id("limit"))
    )
}




