<template>
  <div>
    <div class="cards_item">
      <el-form ref="form_disabled" label-width="120px" size="mini">
        <el-form-item label="预约科室">
          <el-input v-model="form.department" disabled></el-input>
        </el-form-item>

        <el-form-item label="预约医生">
          <el-input v-model="form.doctor" disabled></el-input>
        </el-form-item>

        <el-form-item label="医事医药费">
          <el-input v-model="form.price" disabled></el-input>
        </el-form-item>
      </el-form>
    </div>


    <div class="cards_item">
      <!-- 选择时间 -->
      <el-select label="预约日期:" placeholder="请选择时间" v-model="selectedDate">
        <el-option
            v-for="item in timestamp"
            :key="item.id"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>

      <el-form>
        <el-form-item label="病情描述:">
          <textarea v-model="condition" class="user-input" placeholder="在此输入病情信息"></textarea>
        </el-form-item>

        <el-form-item label="过敏史:">
          <textarea v-model="allergic" class="user-input" placeholder="在此输入过敏史"></textarea>
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
      date: null,
      condition: '',
      allergic: '',
      selectedDate: null,
      timestamp: [],
    }
  },
  created() {
    /** 生成七天日期 */
    const weekIndex = [
      "星期日",
      "星期一",
      "星期二",
      "星期三",
      "星期四",
      "星期五",
      "星期六",
    ]
    let startDate = new Date()
    let dateTemp = new Date()
    for (let i = 0; i < 7; i++){
      dateTemp.setDate(startDate.getDate() + i)
      let week = dateTemp.getDay()
      let label = weekIndex[week] + '||' + dateTemp.toISOString().split('T')[0]
      this.timestamp.push({id: i, label, value:dateTemp.toISOString().split('T')[0] })
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
