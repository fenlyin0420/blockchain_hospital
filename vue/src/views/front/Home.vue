<template>
  <div class="main-content">
    <QRcodeScan @getPrivateKey="data => childData  = data"></QRcodeScan>
    <p>{{ childData }}</p>
  </div>
</template>

<script>
import QRcodeScan from "../component/QRcodeScan.vue";

export default {
  components: {
    QRcodeScan
  },
  data() {
    return {
      childData:""
    }
  },
  methods: {
    update(data) {
      this.childData = data;
    }
  }
}

</script>

<style scoped>

</style>