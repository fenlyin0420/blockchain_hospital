<template>
  <div class="main-content">
    <!-- <QRcodeScan @getPrivateKey="data => childData  = data"></QRcodeScan>
    < -->
    <ProgressBar></ProgressBar>
  </div>
</template>

<script>
import ProgressBar from "../front/ProgressBar";

export default {
  components: {
    ProgressBar
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