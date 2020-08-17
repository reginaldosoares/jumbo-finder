package software.jumbo.storesfinder.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.Resource
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeospatialIndex
import org.springframework.stereotype.Component
import software.jumbo.storesfinder.domain.ListStores
import software.jumbo.storesfinder.domain.StoreDoc
import software.jumbo.storesfinder.repository.StoresRepository
import java.io.InputStream


/**
 * This module run within spring application context loaded,
 * the goal is to parse and load the initial dataset (stores.json)
 * dataset is only loaded when no documents are found in the database.
 */
@Component
class DataLoaderRunner(
    private val repository: StoresRepository,
    @Value("classpath:/stores.json") private val dataSet: Resource,
    @Autowired private val dbOps: MongoTemplate
) : ApplicationRunner {
    private val logger: Logger = getLogger(javaClass)
    override fun run(args: ApplicationArguments) {

        with(repository) {
            if (count().block() == 0L) {
                dataSet.inputStream
                    .getParseJson().stores.toList()
                    .map { storeJson -> storeJson.asDocument() }
                    .let { list -> saveAll(list) }
                    .subscribe { logger.info("New Store loaded: ${it.asJsonResponseOk}") }
            }
        }
        ensureGeoSpatialIndex()
    }

    private fun ensureGeoSpatialIndex() =
        dbOps.indexOps(StoreDoc::class.java)
            .ensureIndex(GeospatialIndex("location").typed(GeoSpatialIndexType.GEO_2DSPHERE))

    private fun InputStream.getParseJson() =
        jacksonObjectMapper().apply {
            configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
        }.readValue<ListStores>(this)
}

