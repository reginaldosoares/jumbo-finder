<template>
  <div id="app">

    <h1>Jumbo Stores Nearby</h1>

    <h2>Click around to see <b>5</b> near jumbo stores up to <b>20 km</b> around you</h2>

    <p> repose your pointer at the store marker for seeing more details about the store! </p>
    <p>thank you! <a target="_blank" href="https://www.jumbo.com">jumbo :)</a></p>

    <GmapMap
        :center="center"
        :zoom="12"
        map-type-id="roadmap"
        style="width: 100%; height: 700px"
        @click="updateCenter"
    >
      <GmapMarker
          :key="index"
          v-for="(m, index) in markers"
          :position="m.position"
          :clickable="true"
          :label="m.label"
          :title="m.title"
      />
      <GmapCircle
          :center="center"
          :radius="500"
          :options="{fillColor:'blue',fillOpacity:0.07}"
      />
    </GmapMap>
    <p>Lat: {{ center.lat }} / Lng: {{ center.lng }}</p>
  </div>
</template>

<script>


// default to Veghel,NL
let initialPosition = {lat: 51.616119, lng: 5.541820}

if (navigator.geolocation && navigator.geolocation.getCurrentPosition) {
  navigator.geolocation.getCurrentPosition(function (position) {
    initialPosition.lat = position.coords.latitude
    initialPosition.lng = position.coords.longitude
  }, function () {
    console.log("Couldn't get your position!");
  });
}

import axios from 'axios'

export default {
  name: 'app',
  data() {
    return {
      // default to Utrecht
      center: initialPosition,
      markers: [],
      places: [],
      currentPlace: null
    }
  },

  methods: {
    async updateCenter(event) {
      // Remove existing markers
      this.markers = []

      this.center = {
        lat: event.latLng.lat(),
        lng: event.latLng.lng(),
      }

      // assuming backend is in the same host
      const currentHost = location.hostname
      const radius = 20
      const limit = 5

      try {
        const results = await axios({
          method: 'get',
          url: `http://${currentHost}:8080/api/stores/nearby/${this.center.lng}/${this.center.lat}?radius=${radius}&limit=${limit}`,
          data: {}
        })

        results.data.map((store) => {
          const point = store.location
          const address = store.address

          let collectionPoint = "no"
          if (store.isCollectionPoint === true) collectionPoint = "yes"

          const marker = {
            label: store.id,
            title: `
              ${store.name} | ${address.city}
              ${address.street}, ${address.number} - ${address.postalCode}
              open hours: ${store.opensAt} - ${store.closesAt}
              is a collection point? ${collectionPoint}
            `,
            position: {
              lat: point.coordinates[1],
              lng: point.coordinates[0]
            }
          }

          this.markers.push(marker)
        })
      } catch (err) {
        // eslint-disable-next-line
        console.err('Error: ', err)
      }
    }
  }
}
</script>

<style>
#app {
  font-family: JumboTheSans, "sans-serif";
  font-size: 16px;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  text-rendering: optimizeLegibility;
  text-shadow: rgba(0, 0, 0, .01) 0 0 1px;
}
</style>
