<template>
  <div id="app">
    <router-view/>
    <floating-character 
      :target-route="''"
      :size="60"
      :drag-threshold="5"
      @click="openMedicalAdvisor"
    />
    <medical-advisor :visible.sync="medicalAdvisorVisible" />
  </div>
</template>

<script>
import FloatingCharacter from '@/views/component/FloatingCharacter.vue'
import MedicalAdvisor from '@/views/component/MedicalAdvisor.vue'

export default {
  name: 'App',
  components: {
    FloatingCharacter,
    MedicalAdvisor
  },
  data() {
    return {
      medicalAdvisorVisible: false
    }
  },
  methods: {
    openMedicalAdvisor() {
      this.medicalAdvisorVisible = true
    }
  }
}
</script>
