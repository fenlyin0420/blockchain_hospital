<template>
  <div>
    <el-row class="info-row" :gutter="24">
      <el-col :span="12">
        <div class="cards_item">
          <el-form ref="form_disabled" label-width="100px" size="mini">
            <el-form-item label="预约科室">
              <el-input v-model="form.department" disabled></el-input>
            </el-form-item>

            <el-form-item label="预约医生">
              <el-input v-model="form.doctor" disabled></el-input>
            </el-form-item>

            <el-form-item label="预约日期">
              <el-input v-model="form.date" disabled></el-input>
            </el-form-item>

            <el-form-item label="挂号费">
              <el-input v-model="form.price" disabled></el-input>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="cards_item">
          <el-form>
            <el-form-item label="病情描述:">
              <div class="el-form-item__content">
                <textarea v-model="condition" class="user-input" placeholder="在此输入病情信息"></textarea>
              </div>
            </el-form-item>

            <el-form-item label="过敏史:">
              <div class="el-form-item__content">
                <textarea v-model="allergic" class="user-input" placeholder="在此输入过敏史"></textarea>
              </div>
            </el-form-item>
          </el-form>

        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  props: {
    form: Object,
  },
  data() {
    return {
      condition: '',
      allergic: '',
    }
  },
  created() {
  },
  methods: {
  },
}
</script>

<style scoped>
.cards_item {
  border-radius: 1px;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.14);
  background-color: #fafafa;
  flex-basis: 25%;
  margin: 8px;
  padding: 24px;
}

.user-input {
  font-family: "SimSun", "宋体", serif;
  font-size: 16px;
  color: blue;
  width: 100%;
  height: 50px;
  margin-bottom: -20px;
}

.el-form-item__content {
  line-height: 31px;
  position: relative;
  font-size: 14px;
}

.el-form-item {
  margin-bottom: 24px;
}

::v-deep .el-input__inner{
  font-family: "SimSun", "宋体", serif;
  font-size: 16px;
  color: blue !important;
}
</style>
