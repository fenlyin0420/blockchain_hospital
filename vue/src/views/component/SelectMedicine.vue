<template>
  <div>
    <!-- 药品穿梭框 -->
    <el-transfer
      v-model="selectedKeys"
      :data="drugList"
      :titles="['药品列表', '已选药品']"
      @change="update"
    >
    </el-transfer>
    <el-form label-position="left" label-width="60px">
      <el-form-item label="数量:">
        <el-input-number v-model="temp_quantity" :min="1" />
      </el-form-item>

      <el-form-item label="频率:">
        <el-select v-model="temp_frequency">
          <el-option label="每日一次" value="每日一次" />
          <el-option label="每日两次" value="每日两次" />
          <el-option label="每日三次" value="每日三次" />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="close">确认药品</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>  
export default {
  props: {
    drugList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
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
      this.$emit("updateDrug", this.selectedDrugs);
    },
    close() {
      this.$emit("close");
    },
  },
};
</script>

<style scoped>
* {
  --el-transfer-panel-width: 40%;
}
.el-form {
  width: 80%; 
  padding-top: 10px;
  display: flex;
  justify-content: space-around;
}
::v-deep .el-transfer-panel {
  width: var(--el-transfer-panel-width);
}
</style>
