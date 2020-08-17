package software.jumbo.storesfinder

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import software.jumbo.storesfinder.domain.StoreDoc

@SpringBootTest
@Disabled
class DataLoaderContextApplicationTests {

	@Autowired
	private lateinit var template: MongoTemplate

	@Test
	fun contextLoads() {
	}

	@Test
	fun `Count loaded dataset after runner executed`() {
		val expectedSize = 587L
		val actualSize = template.query(StoreDoc::class.java).count()

		assertEquals(expectedSize, actualSize)
	}

}
