<template>
  <div>
    <!-- 药品穿梭框 -->
    <el-transfer
      v-model="selectedKeys"
      :data="drugList"
      :titles="['药品列表', '已选药品']"
      @change="update"
    >
      <template #left-footer>
        <el-form label-position="left" label-width="40px">
          <el-form-item label="数量:">
            <el-input-number v-model="temp_quantity" :min="1"/>
          </el-form-item>

          <el-form-item label="频率:">
            <el-select v-model="temp_frequency">
              <el-option label="每日一次" value="每日一次" />
              <el-option label="每日两次" value="每日两次" />
              <el-option label="每日三次" value="每日三次" />
            </el-select>
          </el-form-item>
        </el-form>
      </template>
    </el-transfer>
  </div>
</template>

<script>
export default {
  
  data() {
    return {
      drugList: [
        { key: 1, label: "药品1" },
        { key: 2, label: "药品2" },
        { key: 3, label: "药品3" },
        { key: 4, label: "药品4" },
        { key: 5, label: "药品5" },
        { key: 6, label: "药品6" },
        { key: 7, label: "药品7" },
        { key: 8, label: "药品8" },
        { key: 9, label: "药品9" },
        { key: 10, label: "药品10" },
        { key: 11, label: "药品11"},
        { key: 12, label: "药品12" },
        { key: 13, label: "药品13" },
        { key: 14, label: "药品14" },
        { key: 15, label: "药品15" },
        { key: 16, label: "药品16" },
        { key: 17, label: "药品17" },
        { key: 18, label: "药品18" },
        { key: 19, label: "药品19" },
        { key: 20, label: "药品20" },
      ],
      selectedKeys: [],
      selectedDrugs: [],
      temp_quantity: 1,
      temp_frequency: "每日三次",
    };
  },
  created() {

  },
  methods: {
    /**
     *  to right: update drugList label, push selected drugs to selectedDrugs
     *
     *  to left: reset drugList label, remove selected drugs from selectedDrugs
     * @param values current selected keys
     * @param dir move direction('left' or 'right')
     * @param keys selected keys
     */
    update(values, dir, keys) {
      if (dir === "right") {
        keys.map((key) => {
          const drug = this.drugList.find((drug) => drug.key === key);
          this.selectedDrugs.push({
            ...drug,
            quantity: this.temp_quantity,
            frequency: this.temp_frequency,
          });
        });

        this.drugList.map((item) => {
          if (keys.includes(item.key)) {
            item.label =
              item.label + "-" + this.temp_quantity + "-" + this.temp_frequency;
          }
        });
      } else {
        this.drugList.map((item) => {
          if (keys.includes(item.key)) {
            item.label = item.label.split("-")[0];
          }
        });

        keys.map((key) => {
          const index = this.selectedDrugs.findIndex((drug) => drug.key === key);
          if (index !== -1) {
            this.selectedDrugs.splice(index, 1);
          }
        });
      }
    },
  },
};
</script>

<style scoped>
::v-deep .el-transfer-panel {
  width: 25%;
}
::v-deep .el-transfer-panel__footer {
  height: 200px;
}
</style>
