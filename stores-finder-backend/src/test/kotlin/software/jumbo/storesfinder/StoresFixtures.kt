package software.jumbo.storesfinder

import software.jumbo.storesfinder.domain.StoreJson

object StoresFixtures {

    val dataClassDocStore3178
        get() = StoreJson(
            uuid = "gssKYx4XJwoAAAFbn.BMqPTb",
            sapStoreID = "3178",
            complexNumber = "33010",
            longitude = "4.762433",
            latitude = "52.264417",
            locationType = "SupermarktPuP",
            postalCode = "1431 HN",
            addressName = "Jumbo Changed",
            street = "Ophelialaan",
            street2 = "124",
            street3 = "",
            city = "Aalsmeer",
            collectionPoint = true,
            todayClose = "22:00",
            todayOpen = "07:00",
            showWarningMessage = true
        )


    val rawExistentStore3178
        get() = """{
    "uuid": "gssKYx4XJwoAAAFbn.BMqPTb",
    "complexNumber": "33010",
    "todayOpen": "07:00",
    "longitude": "4.762433",
    "street2": "124",
    "street": "Ophelialaan",
    "addressName": "Jumbo Aalsmeer Ophelialaan",
    "latitude": "52.264417",
    "collectionPoint": true,
    "sapStoreID": "3178",
    "city": "Aalsmeer",
    "postalCode": "1431 HN",
    "todayClose": "22:00",
    "showWarningMessage": true,
    "locationType": "SupermarktPuP",
    "street3": ""}"""

    val rawExistentStore3178Change
        get() = """{
    "uuid": "gssKYx4XJwoAAAFbn.BMqPTb",
    "complexNumber": "33010",
    "todayOpen": "01:00",
    "longitude": "4.762433",
    "street2": "124",
    "street": "test2",
    "addressName": "Jumbo Change1",
    "latitude": "52.264417",
    "collectionPoint": true,
    "sapStoreID": "3178",
    "city": "test1",
    "postalCode": "1431 HN",
    "todayClose": "22:00",
    "showWarningMessage": true,
    "locationType": "SupermarktPuP",
    "street3": ""}"""


    val newRawStore552211
        get() = """{
    "uuid": "gssKYx4XJwoBBBFbn.BMqPTb",
    "complexNumber": "33010",
    "todayOpen": "03:00",
    "longitude": "4.762433",
    "street2": "414",
    "street": "Test One",
    "addressName": "Jumbo New Added",
    "latitude": "52.264417",
    "collectionPoint": true,
    "sapStoreID": "552211",
    "city": "New City",
    "postalCode": "1431 HN",
    "todayClose": "23:00",
    "showWarningMessage": true,
    "locationType": "SupermarktPuP",
    "street3": ""}"""

    val parsedDocStore3178
        get() =
            """{"id":"3178","name":"Jumbo Aalsmeer Ophelialaan","location":{"x":4.762433,"y":52.264417,"coordinates":[4.762433,52.264417],"type":"Point"},"locationType":"SupermarktPuP","address":{"postalCode":"1431 HN","street":"Ophelialaan","number":"124","optional":"","city":"Aalsmeer"},"isCollectionPoint":true,"opensAt":"08:00","closesAt":"22:00"}"""

    val parsedDocStore3501
        get() =
            """{"id":"3501","name":"Jumbo Deventer Eddie Dijkman","location":{"x":6.14273,"y":52.2674,"coordinates":[6.14273,52.2674],"type":"Point"},"locationType":"SupermarktPuP","address":{"postalCode":"7412 MH","street":"Constantijn Huygensstraat","number":"2","optional":"","city":"Deventer"},"isCollectionPoint":true,"opensAt":"08:00","closesAt":"21:00"}"""


    val parsedDocStore3178Success
        get() =
            """{"id": "3178", "name": "Jumbo Aalsmeer Ophelialaan"}"""

    val searchNearbyTest1
        get() = """{"id":"3048","name":"Jumbo Anna Paulowna Molengang","location":{"x":4.83292,"y":52.86143,"type":"Point","coordinates":[4.83292,52.86143]},"locationType":"SupermarktPuP","address":{"postalCode":"1761 BV","street":"Molengang","number":"10-14","optional":"","city":"Anna Paulowna"},"isCollectionPoint":true,"opensAt":"08:00","closesAt":"20:00"}]"""
}
