<template>
  <div id="app">
    <router-view/>
    <floating-character 
      :target-route="'/referral/out'"
      :size="60"
      :drag-threshold="5"
    />
  </div>
</template>

<script>
import FloatingCharacter from '@/views/component/FloatingCharacter.vue'

export default {
  name: 'App',
  components: {
    FloatingCharacter
  }
}
</script>
