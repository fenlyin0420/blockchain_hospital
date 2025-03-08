<!-- 查看一个病历的详细内容， 该页面应包含 <病历> 实体和病历所属的 <用户> 实体-->
<!-- 所以无论从哪里跳转到本页面， 都应该提供上述实体的信息（通过查询参数提供），本页面将使用 receivedData 变量接收-->
<template>
  <el-card class="case-container">
    <el-row class="info-row" :gutter="24">
      <!-- LEFT SIDE -->
      <el-col :span="18">
        <h3 class="title">住院病历/门诊病历</h3>
        <!-- HEAD -->
        <el-row class="info-row" :gutter="24">
          <el-col :span="6">
            <span class="field-label2">姓名:</span>
            <span class="field-value2">{{ receivedData.userName }}</span>
          </el-col>
          <el-col :span="6">
            <span class="field-label2">性别:</span>
            <span class="field-value2">{{ receivedData.sex }}</span>
          </el-col>
          <el-col :span="6">
            <span class="field-label2">年龄:</span>
            <span class="field-value2">{{ receivedData.age }}</span>
          </el-col>
          <el-col :span="6">
            <span class="field-label2">职业:</span>
            <span class="field-value2">{{ receivedData.occupation }}</span>
          </el-col>

          <el-col :span="8">
            <span class="field-label2">入院/就诊时间:</span>
            <span class="field-value2">{{ receivedData.treatmentDate }}</span>
          </el-col>
          <el-col :span="8">
            <span class="field-label2">记录时间:</span>
            <span class="field-value2">{{ receivedData.recordDate }}</span>
          </el-col>

          <el-col :span="8">
            <span class="field-label2">联系方式:</span>
            <span class="field-value2">{{ receivedData.phone }}</span>
          </el-col>
        </el-row>

        <!-- CASE DESCRIPTION -->
        <el-form>
          <div class="field-label">主诉（Chief Complaint, CC）</div>
          <el-form-item>
            <el-input
              :rows="2"
              v-model="receivedData.illnessDetail"
              resize="vertical"
              class="info-textarea CC"
              disabled
            ></el-input>
          </el-form-item>

          <div class="field-label">初步诊断（Primary Diagnosis）</div>
          <el-form-item label="1.主要诊断：">
            <el-input
              v-model="diagnosis1"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="2.次要诊断：">
            <el-input
              v-model="diagnosis2"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>

          <div class="field-label">诊疗计划（Treatment Plan）</div>

          <el-form-item label="进一步检查:">
            <el-input
              v-model="check"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            >
            </el-input>
          </el-form-item>
          <el-form-item label="药物治疗：">
            <el-input class="custom-input" v-model="receivedData.drug" disabled>
              <el-button
                slot="append"
                icon="el-icon-plus"
                @click="showSelectMedicineDialog = true"
                type="primary"
              ></el-button>
            </el-input>
          </el-form-item>
          <el-form-item label="非药物治疗：">
            <el-input
              v-model="non_medicine"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="护理/监测:">
            <el-input
              v-model="care"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="饮食建议：">
            <el-input
              v-model="diet"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
        </el-form>
      </el-col>

      <!-- RIGHT SIDE -->
      <el-col :span="6">
        <div>
          <span class="field-label">辅助检查（Auxiliary Examination）</span>
          <img class="image" :src="receivedData.img" />
          <el-button type="primary" @click="decrypt"> 解密 </el-button>
          <el-button type="primary" @click="verifySign"> 验签 </el-button>
        </div>
      </el-col>
    </el-row>

    <!-- IMAGE PREVIEW DIALOG-->
    <!-- <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" />
    </el-dialog> -->
  </el-card>
</template>

<script>
export default {
  name: "CaseDetail",
  data() {
    return {
      receivedData: {},
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
    };
  },
  created() {
    this.receivedData = this.$route?.query;
    console.log("receivedData", this.receivedData)
    this.load();
  },
  computed: {
    diagnosis1() {
      
      return this.receivedData.diagnosis?.split("\n")[0].split(':')[1];
    },
    diagnosis2() {
      return this.receivedData.diagnosis?.split("\n")[1].split(':')[1];
    },
    check() {
      return this.receivedData.advice?.split("\n")[0].split(':')[1];
    },
    non_medicine() {
      return this.receivedData.advice?.split("\n")[1].split(':')[1];
    },
    care() {
      return this.receivedData.advice?.split("\n")[2].split(':')[1];
    },
    diet() {
      return this.receivedData.advice?.split("\n")[3].split(':')[1];
    },

  },
  methods: {
    load() {
      if (this.receivedData.length) {
        this.$router.push("/caseList");
      }

      // if (this.receivedData.signPubKey != null || this.receivedData.signPubKey != "") {
      //   const s = this.receivedData.signPubKey?.split(",");
      //   const ss = s.map((line) => {
      //     const parts = line?.split(":");
      //     return {
      //       name: parts[0],
      //       key: parts[1],
      //     };
      //   });
      //   this.pubs = ss;
      // }
    },
    /**
     * 病历解密
     * 将本地密文发送到服务端，服务端解密后返回明文
     */
    decrypt() {
      // 解密文字
      let params = {
        userId: this.receivedData.userId,
        advice: this.receivedData.advice,
        drug: this.receivedData.drug,
      };
      this.$request.post("keys/decrypt", params).then((res) => {
        if (res.code === "200") {
          this.receivedData.advice = res.data.advice;
          this.receivedData.drug = res.data.drug;
        } else {
          this.$message.error(res.msg);
        }
      });

      // 解密图片
      const imgUrl = {
        img: this.receivedData.img.slice(0, -1),
      };
      this.$request.post("keys/imgDecrypt", imgUrl).then((res) => {
        if (res.code === "200") {
          this.receivedData.img = `data:image/png;base64,${res.data}`;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    /**
     * 病历验签
     */
    verifySign() {
      if (this.receivedData.id) {
        this.params.id = this.receivedData.id;
        this.$request.post("/keys/verifySign", this.params).then((res) => {
          if (res.code === "200") {
            this.receivedData.signResult = res.data.message;
          } else {
            this.$message.error(res.msg);
          }
        });
      } else {
        console.log(this.receivedData);
        this.$request.post("/keys/verifySignByData", this.receivedData).then((res) => {
          if (res.code === "200") {
            this.receivedData.signResult = res.data.message;
          } else {
            this.$message.error(res.msg);
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.title {
  text-align: center;
}
.case-container {
  min-height: 100%;
}

::v-deep .el-card__body {
  padding-top: 0px;
}

::v-deep .el-row {
  margin: 0px;
}

.custom-col {
  margin-left: 0px;
  /* 减小左侧间距 */
  margin-right: 0px;
  /* 减小右侧间距 */
}

.case-header {
  margin-bottom: 20px;
  text-align: center;
}

.info-row {
  margin-top: 15px;
  margin-bottom: 30px;
}

.info-field {
  margin-bottom: 15px;
}

.field-label {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  margin-right: 10px;
  font-weight: bold;
}

.field-label2 {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  margin-right: 0px;
  width: 80px;
  font-weight: bold;
}

.field-value {
  margin-left: 10px;
  margin-right: 150px;
}

.field-value2 {
  margin-left: 10px;
  margin-right: 0px;
}

.info-textarea {
  width: 100%;
}

::v-deep .el-textarea__inner {
  background-color: transparent;
  resize: none !important;
}

::v-deep .el-textarea__inner::-webkit-scrollbar {
  display: none;
}

.medicine-select {
  width: 50%;
  margin-right: 40px;
}

.quantity-input {
  width: 50%;
  margin-right: 10px;
}

.frequency-input {
  width: 50%;
}

.medicine-textarea {
  margin-top: 10px;
  margin-bottom: 10px;
  width: calc(100% - 10px);
}

.confirm-button {
  display: flex;
  justify-content: center;
  margin: 10px;
}

.edit-button {
  float: right;
  margin-top: 10px;
  margin-right: 10px;
  font-size: 16px;
}

.divider {
  border: 1px solid #dcdcdc;
  margin-bottom: 20px;
}

.el-form-item {
  margin-bottom: 3px;
  display: flex;
}

::v-deep .el-form-item .el-form-item__content {
  flex: 1;
}

::v-deep .el-form-item__label {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  font-weight: bold;
}

.custom-form-inline .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
}
.image {
  width: 100%;
  height: 100%;
}
::v-deep .el-input.is-disabled .el-input__inner {
  background-color: #fff;
}
</style>