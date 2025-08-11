<template>
  <div class="main-content">
    <QRcodeScan active="true" @getPrivateKey="data => childData  = data"></QRcodeScan>
    <
    <!-- <ProgressBar></ProgressBar> -->
  </div>
</template>

<script>
import ProgressBar from "../front/ProgressBar";
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