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
              v-model="receivedData.mainDiagnosis"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="2.次要诊断：">
            <el-input
              v-model="receivedData.secondaryDiagnosis"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>

          <div class="field-label">诊疗计划（Treatment Plan）</div>

          <el-form-item label="进一步检查:">
            <el-input
              v-model="receivedData.furtherCheck"
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
              v-model="receivedData.nonMedicine"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="护理/监测:">
            <el-input
              v-model="receivedData.care"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="饮食建议：">
            <el-input
              v-model="receivedData.diet"
              :rows="1"
              resize="vertical"
              class="info-textarea"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="加密公钥:">
            <el-input
              v-model="receivedData.publicKey"
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
          <el-image :src="receivedData.img" fit="contain">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
          <div class="button-container">
            <el-button type="primary" @click="encrypt"> 加密 </el-button>
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
    };
  },
  created() {
    console.log("CaseEncrypt receivedData", this.receivedData)
    this.load();
  },
  computed: {
    receivedData() {
      return this.$store.state.traverseData
    },
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
          secondaryDiagnosis: this.receivedData.secondaryDiagnosis,
          drug: this.receivedData.drug,
          furtherCheck: this.receivedData.furtherCheck,
          nonMedicine: this.receivedData.nonMedicine,
          care: this.receivedData.care,
          diet: this.receivedData.diet,
          img: this.receivedData.img,
        })
        .then((res) => {
          if (res.code === "200") {
            console.log("加密后：", res);
            // 更新数据为加密后的内容
            this.receivedData.mainDiagnosis = res.data.mainDiagnosis;
            this.receivedData.secondaryDiagnosis = res.data.secondaryDiagnosis;
            this.receivedData.drug = res.data.drug;
            this.receivedData.furtherCheck = res.data.furtherCheck;
            this.receivedData.nonMedicine = res.data.nonMedicine;
            this.receivedData.care = res.data.care;
            this.receivedData.diet = res.data.diet;
            this.receivedData.img = res.data.img + "?t=" + new Date().getTime();
            // 将个人信息等用base64编码，以呈现加密效果
            this.receivedData.userName = this.stob(this.receivedData.userName)
            this.receivedData.sex = this.stob(this.receivedData.sex)
            this.receivedData.age = this.stob(this.receivedData.age)
            this.receivedData.occupation = this.stob(this.receivedData.occupation)
            this.receivedData.treatmentDate = this.stob(this.receivedData.treatmentDate)
            this.receivedData.recordDate = this.stob(this.receivedData.recordDate)
            this.receivedData.phone = this.stob(this.receivedData.phone)
            this.receivedData.illnessDetail = this.stob(this.receivedData.illnessDetail)
            this.sign();

          } else {
            this.$message.error("加密失败 :(");
            console.log(res)
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
            this.upChain();
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
      for (const key in this.receivedData) {
        if (this.receivedData[key] == null || this.receivedData[key] == "") {
          this.receivedData[key] = "无";
        }
      }
      let timestamp = new Date().getTime();
      let traverse = {
        idCard: this.receivedData.idCard,
        patientData: this.receivedData.userName + "||" + this.receivedData.sex + "||" + this.receivedData.age + "||" + this.receivedData.occupation + "||" + this.receivedData.phone + "||" + this.receivedData.treatmentDate + "||" + this.receivedData.recordDate + "||" + timestamp,
        illnessDetail: this.receivedData.illnessDetail,
        diagnosisData: this.receivedData.mainDiagnosis + "||" + this.receivedData.secondaryDiagnosis,
        treatmentPlan: this.receivedData.furtherCheck + "||" + this.receivedData.drug + "||" + this.receivedData.nonMedicine + "||" + this.receivedData.care + "||" + this.receivedData.diet,
        hospitalInfo: this.receivedData.hospitalName + "||" + this.receivedData.doctorName,
        img: this.receivedData.img,
        inHospitalStatus: this.receivedData.inHospital,
        signData: this.signData,
      }
      console.log("traverse:", traverse)

      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });

      // 上传区块链
      Request.post("/storeMedicalRecord", traverse).then((res) => {
        if (res.data.code === "200") {
          this.transactionHash = res.data.data.transactionReceipt.transactionHash;
          console.log("res:", res)
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
    /**
     * 将原始字符串编码为base64字符串
     * @param s 原始字符串
     * @return base64字符串
     */
    stob(s) {
      const utf8Str = encodeURIComponent(s).replace(/%([0-9A-F]{2})/g, (match, p1) => {
          return String.fromCharCode(parseInt(p1, 16));
      });
      return btoa(utf8Str);
    },

    /**
     * 将base64字符串解码为原始字符串
     * 
     * @param b base64 字符串
     * @return 原始字符串
     */
    btos(b) {
      const utf8Str = atob(b);
      return decodeURIComponent(Array.prototype.map.call(utf8Str, (c) => {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));
    }
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