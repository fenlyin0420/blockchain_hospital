<!-- 查看患者的病历历史记录，从区块链获取多份病历信息，通过标签栏分隔显示 -->
<template>
  <el-card class="case-container">
    <div v-if="loading" class="loading-container">
      <el-progress type="circle" :percentage="progressPercentage"></el-progress>
      <p>正在从区块链获取病历数据，请稍候...</p>
    </div>
    
    <div v-else>
      <h2 class="title">患者病历历史记录</h2>
      
      <!-- 如果没有病历数据 -->
      <div v-if="medicalRecords.length === 0" class="no-data">
        <el-empty description="未找到病历记录"></el-empty>
      </div>
      
      <!-- 如果有病历数据 -->
      <div v-else>
        <!-- 标签栏 -->
        <el-tabs v-model="activeTabName" type="card" @tab-click="handleTabClick">
          <el-tab-pane 
            v-for="(record, index) in medicalRecords" 
            :key="index"
            :label="'病历 ' + (index + 1) + ' (' + record.treatmentDate + ')'"
            :name="'tab-' + index"
          >
            <!-- 病历详情内容 -->
            <el-row class="info-row" :gutter="24">
              <!-- LEFT SIDE -->
              <el-col :span="18">
                <h3 class="subtitle">住院病历/门诊病历</h3>
                <!-- HEAD -->
                <el-row class="info-row" :gutter="24">
                  <el-col :span="6">
                    <span class="field-label2">姓名:</span>
                    <span class="field-value2">{{ record.userName }}</span>
                  </el-col>
                  <el-col :span="6">
                    <span class="field-label2">性别:</span>
                    <span class="field-value2">{{ record.sex }}</span>
                  </el-col>
                  <el-col :span="6">
                    <span class="field-label2">年龄:</span>
                    <span class="field-value2">{{ record.age }}</span>
                  </el-col>
                  <el-col :span="6">
                    <span class="field-label2">职业:</span>
                    <span class="field-value2">{{ record.occupation }}</span>
                  </el-col>

                  <el-col :span="8">
                    <span class="field-label2">入院/就诊时间:</span>
                    <span class="field-value2">{{ record.treatmentDate }}</span>
                  </el-col>
                  <el-col :span="8">
                    <span class="field-label2">记录时间:</span>
                    <span class="field-value2">{{ record.recordDate }}</span>
                  </el-col>

                  <el-col :span="8">
                    <span class="field-label2">联系方式:</span>
                    <span class="field-value2">{{ record.phone }}</span>
                  </el-col>
                </el-row>

                <!-- CASE DESCRIPTION -->
                <el-form>
                  <div class="field-label">主诉（Chief Complaint, CC）</div>
                  <el-form-item>
                    <el-input
                      :rows="2"
                      v-model="record.illnessDetail"
                      resize="vertical"
                      class="info-textarea CC"
                      disabled
                    ></el-input>
                  </el-form-item>

                  <div class="field-label">初步诊断（Primary Diagnosis）</div>
                  <el-form-item label="1.主要诊断：">
                    <el-input
                      v-model="record.mainDiagnosis"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="2.次要诊断：">
                    <el-input
                      v-model="record.secondaryDiagnosis"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    ></el-input>
                  </el-form-item>

                  <div class="field-label">诊疗计划（Treatment Plan）</div>

                  <el-form-item label="进一步检查:">
                    <el-input
                      v-model="record.furtherCheck"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    >
                    </el-input>
                  </el-form-item>
                  <el-form-item label="药物治疗：">
                    <el-input class="custom-input" v-model="record.drug" disabled>
                    </el-input>
                  </el-form-item>
                  <el-form-item label="非药物治疗：">
                    <el-input
                      v-model="record.nonMedicine"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="护理/监测:">
                    <el-input
                      v-model="record.care"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="饮食建议：">
                    <el-input
                      v-model="record.diet"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="签名数据：">
                    <el-input
                      v-model="record.signData"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="验签结果：">
                    <el-input
                      v-model="verifySignResult"
                      v-if="verifySignResult === '成功'"
                      :rows="1"
                      resize="vertical"
                      class="info-textarea"
                      disabled
                    ></el-input>
                    <el-input
                      v-model="verifySignResult"
                      v-else
                      :rows="1"
                      resize="vertical"
                      class="info-textarea is-error"
                      disabled
                    ></el-input>
                  </el-form-item>
                </el-form>
              </el-col>

              <!-- RIGHT SIDE -->
              <el-col :span="6">
                <div>
                  <span class="right-field-label">辅助检查（Auxiliary Examination）</span>
                  <img v-if="record.img" class="image" :src="record.img" />
                  <el-empty v-else description="无医疗影像" :image-size="100"></el-empty>
                  <div class="button-container">
                    <el-button type="primary" @click="init(record)"> 解密 </el-button>
                    <el-button type="primary" @click="verifySign(record)"> 验签 </el-button>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <el-dialog :visible.sync="showQRcodeScan" @closed="decrypt">
      <QRcodeScan @getPrivateKey="data => privateKey = data"></QRcodeScan>
    </el-dialog>
  </el-card>
</template>

<script>
import axios from "axios";
import QRcodeScan from "../component/QRcodeScan.vue";

export default {
  name: "CaseHistory",
  components: {
    QRcodeScan
  },
  data() {
    return {
      loading: true,
      progressPercentage: 0,
      idCard: "",
      medicalRecords: [],
      activeTabName: "tab-0",
      currentRecord: null,
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      verifySignResult: "未验签",
      showQRcodeScan: false,
      privateKey: "",
    };
  },
  created() {
    // 从路由参数获取身份证号
    this.idCard = this.$route.query.idCard;
    
    if (!this.idCard) {
      this.$message.error("未提供身份证号，无法获取病历记录");
      this.loading = false;
      return;
    }
    
    // 从区块链获取病历记录
    this.fetchMedicalRecords();
  },
  methods: {
    /**
     * 从区块链获取病历记录
     */
    fetchMedicalRecords() {
      this.loading = true;
      this.progressPercentage = 30;
      
      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });
      
      Request.post("/getMedicalRecordsByIdCard", { _idCard: this.idCard })
        .then(res => {
          this.progressPercentage = 70;
          
          if (res.data.code === "200") {
            // 解析数据
            const parsedRecords = this.parseTraverse(res.data.data.returnObject);
            this.medicalRecords = parsedRecords;
            
            if (parsedRecords.length > 0) {
              this.activeTabName = "tab-0";
              this.currentRecord = parsedRecords[0];
            }
            
            this.$message.success(`成功获取 ${parsedRecords.length} 条病历记录`);
          } else {
            this.$message.error("获取病历记录失败: " + (res.data.msg || "未知错误"));
          }
        })
        .catch(error => {
          console.error("获取病历记录错误:", error);
          this.$message.error("获取病历记录失败: " + (error.message || "网络错误"));
        })
        .finally(() => {
          this.progressPercentage = 100;
          this.loading = false;
        });
    },
    
    /**
     * 解析为病历列表
     * @param dataList 病历列表
     */
    parseTraverse(dataList) {
      return dataList.flatMap((item) => {
        return item
          .split("\n\n\n")
          .filter((entry) => entry.trim() !== "")
          .map((entry) => {
            const lines = entry.split("\n").filter((line) => line.trim() !== "");
            const obj = {};
            
            // 提取记录编号
            const recordMatch = entry.match(/----------- 记录 (\d+) -----------/);
            if (recordMatch) {
              obj.recordNumber = recordMatch[1];
            }
            
            // 提取各部分信息
            let currentSection = "";
            
            lines.forEach((line) => {
              line = line.trim();
              if (!line) return;
              
              // 检查是否是部分标题
              if (line.includes("【患者信息】")) {
                currentSection = "patient";
                return;
              } else if (line.includes("【主诉症状】")) {
                currentSection = "symptoms";
                return;
              } else if (line.includes("【诊断结果】")) {
                currentSection = "diagnosis";
                return;
              } else if (line.includes("【诊疗计划】")) {
                currentSection = "treatment";
                return;
              } else if (line.includes("【医院信息】")) {
                currentSection = "hospital";
                return;
              }
              
              // 解析患者信息部分的特殊格式
              if (currentSection === "patient" && line.includes("\t|\t")) {
                const parts = line.split("\t|\t");
                parts.forEach(part => {
                  const [key, value] = part.split(": ");
                  if (key && value) {
                    switch(key.trim()) {
                      case "姓名":
                        obj.userName = value.trim();
                        break;
                      case "性别":
                        obj.sex = value.trim();
                        break;
                      case "年龄":
                        obj.age = value.trim();
                        break;
                      case "职业":
                        obj.occupation = value.trim();
                        break;
                      case "电话":
                        obj.phone = value.trim();
                        break;
                    }
                  }
                });
                return;
              }
              
              // 解析就诊日期和记录时间
              if (currentSection === "patient" && line.includes("就诊日期:") && line.includes("记录时间:")) {
                const parts = line.split("\t|\t");
                parts.forEach(part => {
                  const [key, value] = part.split(": ");
                  if (key && value) {
                    switch(key.trim()) {
                      case "就诊日期":
                        obj.treatmentDate = value.trim();
                        break;
                      case "记录时间":
                        obj.recordDate = value.trim();
                        break;
                    }
                  }
                });
                return;
              }
              
              // 解析时间戳
              if (currentSection === "patient" && line.includes("时间戳:")) {
                const [key, value] = line.split(": ");
                if (key && value) {
                  obj.timestamp = value.trim();
                }
                return;
              }
              
              // 解析主诉症状
              if (currentSection === "symptoms" && line.includes("【主诉症状】")) {
                obj.illnessDetail = line.split("【主诉症状】")[1].trim();
                return;
              }
              
              // 解析诊断结果
              if (currentSection === "diagnosis" && line.includes("主要诊断:")) {
                obj.mainDiagnosis = line.split("主要诊断:")[1].trim();
                return;
              }
              
              // 解析诊疗计划
              if (currentSection === "treatment") {
                if (line.includes("检查项目:")) {
                  obj.furtherCheck = line.split("检查项目:")[1].trim();
                } else if (line.includes("非药物治疗:")) {
                  obj.nonMedicine = line.split("非药物治疗:")[1].trim();
                }
                return;
              }
              
              // 解析医院信息
              if (currentSection === "hospital") {
                if (line.includes("医院名称:")) {
                  obj.hospitalName = line.split("医院名称:")[1].trim();
                } else if (line.includes("主治医师:")) {
                  obj.doctorName = line.split("主治医师:")[1].trim();
                }
                return;
              }
              
              // 解析其他键值对
              const colonIndex = line.indexOf(":");
              if (colonIndex !== -1) {
                const key = line.slice(0, colonIndex).trim();
                const value = line.slice(colonIndex + 1).trim();
                
                switch(key) {
                  case "身份证号":
                    obj.idCard = value;
                    break;
                  case "【医疗影像】":
                    obj.img = value;
                    break;
                  case "【住院状态】":
                    obj.inHospital = value;
                    break;
                  case "【数字签名】":
                    obj.signData = value;
                    break;
                }
              }
            });
            
            return obj;
          });
      });
    },
    
    /**
     * 处理标签页点击事件
     */
    handleTabClick(tab) {
      const index = parseInt(tab.name.split('-')[1]);
      this.currentRecord = this.medicalRecords[index];
    },
    
    /**
     * 初始化解密
     */
    init(record) {
      this.currentRecord = record;
      if (this.user.role !== "USER"){
        this.showQRcodeScan = true;
      } else {
        this.decrypt();
      }
    },
    
    /**
     * 病历解密
     * 将本地密文发送到服务端，服务端解密后返回明文
     */
    decrypt() {
      if (!this.currentRecord) return;
      
      // 解密文字
      let params = {
        userId: this.currentRecord.userId,
        advice: this.currentRecord.advice,
        drug: this.currentRecord.drug,
      };

      if (this.user.role === "USER") {
        this.$request.post("keys/decrypt", params).then((res) => {
          if (res.code === "200") {
            this.currentRecord.advice = res.data.advice;
            this.currentRecord.drug = res.data.drug;
          } else {
            this.$message.error(res.msg);
          }
        });
      } else {
        this.$request.post("keys/decryptByQR", params, {
          params:{
            QR: this.privateKey
          }
        }).then((res) => {
          if (res.code === "200") {
            this.currentRecord.advice = res.data.advice;
            this.currentRecord.drug = res.data.drug;
          } else {
            this.$message.error(res.msg);
          }
        });
      }
    },
    
    /**
     * 病历验签
     */
    verifySign(record) {
      this.currentRecord = record;
      
      if (this.currentRecord.id) {
        this.$request.post("/keys/verifySign", this.currentRecord).then((res) => {
          if (res.code === "200") {
            this.$message.success("验签成功");
            this.verifySignResult = res.data.message;
          } else {
            this.$message.error(res.msg);
          }
        });
      } else {
        this.$request.post("/keys/blockchain/sign", this.currentRecord, {
          params:{
            doctorId: this.user.id
          }
        }).then((res) => {
          if (res.code === "200") {
            this.verifySignResult = res.data.message;
          } else {
            this.$message.error(res.msg);
          }
        });
      }
    }
  }
};
</script>

<style scoped>
.title {
  text-align: center;
  margin-bottom: 20px;
}

.subtitle {
  text-align: center;
}

.case-container {
  min-height: 100%;
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 300px;
}

.no-data {
  display: flex;
  justify-content: center;
  margin-top: 50px;
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