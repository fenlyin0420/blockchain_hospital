<template>
  <div>
    <div class="cards_item">
      <el-form ref="form_disabled" :model="sizeForm" label-width="120px" size="mini">
        <el-form-item label="预约科室">
          <el-input v-model="sizeForm.department" disabled></el-input>
        </el-form-item>

        <el-form-item label="预约医生">
          <el-input v-model="sizeForm.doctor" disabled></el-input>
        </el-form-item>

        <el-form-item label="预约时间">
          <el-input v-model="sizeForm.date" disabled></el-input>
        </el-form-item>

        <el-form-item label="医事医药费">
          <el-input v-model="sizeForm.price" disabled></el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="cards_item">
      <el-form ref="form" :model="sizeForm" label-width="120px" size="mini">
        <el-form-item label="病情描述">
          <textarea v-model="sizeForm.condition" class="user-input" placeholder="在此输入病情信息"></textarea>
        </el-form-item>

        <el-form-item label="过敏史">
          <textarea v-model="sizeForm.allergic" class="user-input" placeholder="在此输入过敏史"></textarea>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    form: Object,
  },
  data() {
    return {
      sizeForm: {
        department: this.form.department,
        doctor: this.form.doctor,
        date: this.form.date,
        price: this.form.price,
        condition: '',
        allergic: ' ',
      },
    }
  },
  methods: {
  },
}
</script>

<style scoped>
.cards_item {
  border-radius: 5px;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14);
  background-color: #fafafa;
  flex-basis: 25%;
  margin: 8px;
  padding: 24px;
}
.user-input {
  width: 100%;
  height: 50px;
}
</style>
