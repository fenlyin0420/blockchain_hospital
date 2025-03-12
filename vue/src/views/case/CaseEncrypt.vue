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
          <span class="right-field-label">辅助检查（Auxiliary Examination）</span>
          <img class="image" :src="receivedData.img" />
          <div class="button-container">
            <el-button type="primary" @click="encrypt"> encrypt </el-button>
          </div>
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
import axios from "axios";

export default {
  data() {
    return {
      /** 接收查询参数 */
      receivedData: {},
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      pubs: {},
      fits: "fill",
      dialogVisible: false, // 对话框可见性
      previewImageUrl: "", // 预览图片URL
      previewImageIndex: -1, // 预览图片索引
      /** 交易哈希 */
      transactionHash: "NULL",
      QRURL: "",
      params: {},
      signData: "",
      signPubKey: "",
      img: "",
    };
  },
  created() {
    this.receivedData = this.$route.query;
    this.img = this.receivedData.img;
    console.log(this.receivedData)
    this.load();
  },
  computed: {
    ImageLines() {
      const urlImageString = this.receivedData.img;
      const urlImageLines = urlImageString.split("\n");
      if (urlImageLines.length > 0 && urlImageLines[urlImageLines.length - 1] === "") {
        urlImageLines.pop();
      }
      return urlImageLines;
    },
  },
  methods: {
    //图片预览
    previewImage(url, index) {
      this.previewImageUrl = url;
      this.previewImageIndex = index;
      this.dialogVisible = true;
    },

    load() {
      if (this.receivedData.length) {
        this.$router.push("/caseList");
      }

      if (this.receivedData.signPubKey != null) {
        const s = this.receivedData.signPubKey.split(",");
        const ss = s.map((line) => {
          const parts = line.split(":");
          return {
            name: parts[0],
            key: parts[1],
          };
        });
        this.pubs = ss;
      }
    },
    /**
     * 加密病历文字字段
     * TODO: 文字与图片一起加密
     */
    encrypt() {
      this.$request
        .post("keys/encrypt", {
          id: this.receivedData.id,
          userId: this.receivedData.userId,
          mainDiagnosis: this.receivedData.mainDiagnosis,
          secondaryDiagnosis: this.secondaryDiagnosis,
          drug: this.receivedData.drug,
          check: this.receivedData.check,
          nonMedicine: this.receivedData.nonMedicine,
          care: this.receivedData.care,
          diet: this.receivedData.diet,
          img: this.receivedData.img,
        })
        .then((res) => {
          if (res.code === "200") {
            // 更新数据为加密后的内容
            this.receivedData.mainDiagnosis = res.data.mainDiagnosis;
            this.receivedData.secondaryDiagnosis = res.data.secondaryDiagnosis;
            this.receivedData.drug = res.data.drug;
            this.check = res.data.check;
            this.nonMedicine = res.data.nonMedicine;
            this.care = res.data.care;
            this.diet = res.data.diet;
            this.receivedData.img = res.data.img
            console.log("res", res)
            // this.sign();
          } else {
            this.$message.error("加密失败 :(");
          }
        });
    },
    sign() {
      this.$request
        .post("/keys/blockchain/sign", this.receivedData, {
          params: {
            doctorId: this.user.id,
          },
        })
        .then((res) => {
          if (res.code === "200") {
            this.signData = res.data.signData;
            this.signPubKey = res.data.signPubKey;
            // this.upChain();
          } else {
            this.$message.error("签名失败 :(");
          }
        });
    },

    /**
     * 将病历上传至区块链
     */
    async upChain() {
      this.params.id = this.receivedData.id;
      // 构造要上传的至区块链的 JSON 病历
      let traverse = {
        _idCard: this.receivedData.idCard,
        _userHospitalDoctor:
          this.receivedData.userName +
          "||" +
          this.receivedData.hospitalName +
          "||" +
          this.receivedData.doctorName,
        _timestampIllness:
          this.receivedData.timestamp + "||" + this.receivedData.illnessDetail,
        _treatmentRecordDate:
          this.receivedData.treatmentDate + "||" + this.receivedData.recordDate,
        _inHospital: this.receivedData.inHospital,
        _drugAdvice: this.receivedData.drug + "||" + this.receivedData.advice,
        _diagnosis: this.receivedData.diagnosis,
        // _img: "http://localhost:8090/files/default.jpg",
        _img: this.receivedData.img,
        _signData: this.signData,
        _signPubKey: this.signPubKey,
      };
      console.log(traverse)
      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });

      // 上传区块链
      Request.post("/storeMedicalRecord", traverse).then((res) => {
        if (res.data.code === "200") {
          this.transactionHash = res.data.data.transactionReceipt.transactionHash;
          console.log(res)
          this.generateQR();
          this.$message.success("上传区块链成功 :)");
        } else {
          this.$message.error("上传区块链失败 :(");
        }
      });
    },
    generateQR() {
      this.$request
        .get("/files/generateQR", {
          params: {
            seed: this.transactionHash,
          },
        })
        .then((res) => {
          if (res.code === "200") {
            this.QRURL = res.data;
          }
        });
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
  margin: 20px 0;
  width: 100%;
  height: 100%;
}
::v-deep .el-input.is-disabled .el-input__inner {
  background-color: #fff;
}
.button-container {
  display: flex;
  flex-direction: column;
  margin-top: 50px;
}
.button-container .el-button {
  margin: 10px auto;
  width: 80%;
}
.right-field-label {
  font-family: "SimHei", "黑体", sans-serif;
  font-size: 16px;
  margin-bottom: 20px;
  font-weight: bold;
}

</style>