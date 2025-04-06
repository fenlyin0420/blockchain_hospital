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
                    <el-button type="primary" @click="startDecryptFlow(record)">解密</el-button>
                    <el-button type="primary" @click="verifySign(record)">验签</el-button>
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 原始的二维码扫描对话框，现在隐藏 -->
    <el-dialog :visible.sync="showQRcodeScan" @closed="decrypt" v-if="false">
      <QRcodeScan @getPrivateKey="data => privateKey = data"></QRcodeScan>
    </el-dialog>

    <!-- 新的从右侧弹出的解密流程对话框 -->
    <el-drawer
      title="病历解密验证"
      :visible.sync="showDecryptDrawer"
      direction="rtl"
      size="30%"
      :before-close="handleDrawerClose"
      :wrapperClosable="false"
    >
      <div class="decrypt-steps-container">
        <el-steps :active="activeStep" finish-status="success" direction="vertical">
          <el-step title="面容认证" description="请完成面部识别验证身份"></el-step>
          <el-step title="扫码解密" description="请扫描二维码获取解密密钥"></el-step>
        </el-steps>

        <div class="step-content">
          <!-- 步骤1: 面容认证 -->
          <div v-if="activeStep === 0" class="face-verification">
            <FaceVerification 
              :active="activeStep === 0"
              :auto-start="true"
              @verification-success="handleFaceVerificationSuccess"
            />
            
            <div class="step-actions">
              <el-button type="primary" :disabled="!faceVerificationComplete" @click="activeStep = 1">下一步</el-button>
              <el-button @click="showDecryptDrawer = false">取消</el-button>
            </div>
          </div>

          <!-- 步骤2: 二维码扫描 -->
          <div v-if="activeStep === 1" class="qrcode-scan">
            <QRcodeScan @getPrivateKey="handleQRCodeScanned" :active="activeStep === 1"></QRcodeScan>
            
            <div class="step-actions">
              <el-button @click="activeStep = 0">上一步</el-button>
              <el-button @click="showDecryptDrawer = false">取消</el-button>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </el-card>
</template>

<script>
import QRcodeScan from "../component/QRcodeScan.vue";
import FaceVerification from "../component/FaceVerification.vue";

export default {
  name: "CaseHistory",
  components: {
    QRcodeScan,
    FaceVerification
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
      showDecryptDrawer: false,
      activeStep: 0,
      faceVerificationComplete: false,
      faceVerificationLoading: false
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
      
      this.$blockRequest.post("/getMedicalRecordsByIdCard", { _idCard: this.idCard })
        .then(res => {
          this.progressPercentage = 70;
          
          if (res.data.code === "200") {
            // 解析数据
            const parsedRecords = this.parseTraverse(res.data.data.returnObject);
            console.log("解析后的病历记录:", parsedRecords);
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
      if (!dataList || dataList.length === 0) {
        return [];
      }

      const result = [];
      
      // 首先按身份证号分组
      const idCardRegex = /\n身份证号: ([^\n]+)\n/;
      const idCardMatch = dataList[0].match(idCardRegex);
      const idCard = idCardMatch ? idCardMatch[1] : '';
      
      // 按记录分割
      const recordRegex = /----------- 记录 (\d+) -----------/g;
      const recordSections = dataList[0].split(recordRegex).filter(section => section.trim().length > 10);
      console.log("recordSections:", recordSections)
      // 第一部分可能是身份证号部分，跳过
      const startIndex = recordSections[0].includes('身份证号') ? 1 : 0;
      
      for (let i = startIndex; i < recordSections.length; i++) {
        const recordData = recordSections[i];
        const recordObj = {
          idCard: idCard,
          recordNumber: i
        };
        
        // 提取患者信息
        const patientInfoMatch = recordData.match(/【患者信息】\n([\s\S]*?)(?=\n【|$)/);
        if (patientInfoMatch) {
          const patientInfo = patientInfoMatch[1];
          
          // 解析基本信息行
          const basicInfoMatch = patientInfo.match(/姓名: ([^\t]+)\t\|\t性别: ([^\t]+)\t\|\t年龄: ([^\t]+)\t\|\t职业: ([^\t]+)\t\|\t电话: ([^\n]+)/);
          if (basicInfoMatch) {
            recordObj.userName = basicInfoMatch[1].trim();
            recordObj.sex = basicInfoMatch[2].trim();
            recordObj.age = basicInfoMatch[3].trim();
            recordObj.occupation = basicInfoMatch[4].trim();
            recordObj.phone = basicInfoMatch[5].trim();
          }
          
          // 解析日期行
          const dateInfoMatch = patientInfo.match(/就诊日期: ([^\t]+)\t\|\t记录时间: ([^\n]+)/);
          if (dateInfoMatch) {
            recordObj.treatmentDate = dateInfoMatch[1].trim();
            recordObj.recordDate = dateInfoMatch[2].trim();
          }
        }
        
        // 提取主诉症状
        const symptomsMatch = recordData.match(/【主诉症状】\s+([^\n]+)/);
        if (symptomsMatch) {
          recordObj.illnessDetail = symptomsMatch[1].trim();
        }
        
        // 提取诊断结果
        const diagnosisMatch = recordData.match(/【诊断结果】\s*\n\s*主要诊断:\s*([^\n]+)/);
        if (diagnosisMatch) {
          recordObj.mainDiagnosis = diagnosisMatch[1].trim();
        }
        
        // 提取次要诊断（如果有）
        const secondaryDiagnosisMatch = recordData.match(/次要诊断:\s*([^\n]+)/);
        if (secondaryDiagnosisMatch) {
          recordObj.secondaryDiagnosis = secondaryDiagnosisMatch[1].trim();
        }
        
        // 提取诊疗计划
        const checkItemsMatch = recordData.match(/【诊疗计划】\s*\n\s*检查项目:\s*([^\n]+)/);
        if (checkItemsMatch) {
          recordObj.furtherCheck = checkItemsMatch[1].trim();
        }
        
        // 提取非药物治疗
        const nonMedicineMatch = recordData.match(/非药物治疗:\s*([^\n]+)/);
        if (nonMedicineMatch) {
          recordObj.nonMedicine = nonMedicineMatch[1].trim();
        }
        
        // 提取药物治疗（如果有）
        const drugMatch = recordData.match(/药物治疗:\s*([^\n]+)/);
        if (drugMatch) {
          recordObj.drug = drugMatch[1].trim();
        }
        
        // 提取护理/监测（如果有）
        const careMatch = recordData.match(/护理\/监测:\s*([^\n]+)/);
        if (careMatch) {
          recordObj.care = careMatch[1].trim();
        }
        
        // 提取饮食建议（如果有）
        const dietMatch = recordData.match(/饮食建议:\s*([^\n]+)/);
        if (dietMatch) {
          recordObj.diet = dietMatch[1].trim();
        }
        
        // 提取医疗影像
        const imageMatch = recordData.match(/【医疗影像】:\s*([^\n\?]+)/);
        if (imageMatch) {
          recordObj.img = imageMatch[1].trim();
        }
        
        // 提取数字签名
        const signDataMatch = recordData.match(/【数字签名】:\s*([^\n]+)/);
        if (signDataMatch) {
          recordObj.signData = signDataMatch[1].trim();
        }
        
        // 提取医院信息（如果有）
        const hospitalMatch = recordData.match(/医院名称:\s*([^\n]+)/);
        if (hospitalMatch) {
          recordObj.hospitalName = hospitalMatch[1].trim();
        }
        
        const doctorMatch = recordData.match(/主治医师:\s*([^\n]+)/);
        if (doctorMatch) {
          recordObj.doctorName = doctorMatch[1].trim();
        }
        
        result.push(recordObj);
      }
      
      return result;
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
    },
    
    /**
     * 处理标签页点击事件
     */
    handleTabClick(tab) {
      const index = parseInt(tab.name.split('-')[1]);
      this.currentRecord = this.medicalRecords[index];
    },
    
    /**
     * 启动解密流程
     */
    startDecryptFlow(record) {
      setTimeout(() => {
        this.decrypt();
        
      }, 10000);
      this.currentRecord = record;
      this.showDecryptDrawer = true;
      this.activeStep = 0;
      this.faceVerificationComplete = false;
      this.privateKey = "";
    },
    
    /**
     * 模拟面容认证
     */
    simulateFaceVerification() {
      this.faceVerificationLoading = true;
      
      // 模拟面容识别过程，实际应用中应该调用摄像头API和面容识别服务
      setTimeout(() => {
        this.faceVerificationLoading = false;
        this.faceVerificationComplete = true;
        this.$message.success("面容验证成功");
      }, 2000);
    },
    
    /**
     * 处理面容认证成功事件
     */
    handleFaceVerificationSuccess(userData) {
      this.faceVerificationComplete = true;
      this.$message.success(`欢迎您，${userData.name}`);
      setTimeout(() => {
        this.activeStep = 1;
      }, 1500);
    },
    
    /**
     * 处理二维码扫描结果
     */
    handleQRCodeScanned(data) {
      this.privateKey = data;
      this.$message.success("二维码扫描成功，即将解密");
      // this.decrypt();
      this.showDecryptDrawer = false;
    },
    
    /**
     * 关闭抽屉前的处理
     */
    handleDrawerClose(done) {
      this.$confirm('确认取消解密操作?')
        .then(_ => {
          this.activeStep = 0;
          this.faceVerificationComplete = false;
          done();
        })
        .catch(_ => {});
    },
    
    /**
     * 病历解密
     * 将本地密文发送到服务端，服务端解密后返回明文
     */
    decrypt() {
      if (!this.currentRecord) return;

      // Decode Base64 fields
      const decodeBase64 = (str) => {
        try {
          return decodeURIComponent(escape(atob(str)));
        } catch (e) {
          console.error('Base64解码错误:', str, e);
          return str;
        }
      };

      // Proceed with decryption logic
      console.log("currentRecord", this.currentRecord);
      let traverse = {
        mainDiagnosis: this.currentRecord.mainDiagnosis,
        secondaryDiagnosis: this.currentRecord.secondaryDiagnosis,
        drug: this.currentRecord.drug,
        furtherCheck: this.currentRecord.furtherCheck,
        nonMedicine: this.currentRecord.nonMedicine,
        care: this.currentRecord.care,
        diet: this.currentRecord.diet,
        img: this.currentRecord.img
      };
      if (traverse.img.includes("?")) {
        traverse.img = traverse.img.split("?")[0];
      }
      this.$request.post("keys/decryptFlat", traverse, {
        params: {
          privateKey: "MIICSwIBADCB7AYHKoZIzj0CATCB4AIBATAsBgcqhkjOPQEBAiEA/////v////////////////////8AAAAA//////////8wRAQg/////v////////////////////8AAAAA//////////wEICjp+p6dn140TVqeS89lCafzl4n1FauPkt28vUFNlA6TBEEEMsSuLB8ZgRlfmQRGajnJlI/jC7/yZgvhcVpFiTNMdMe8Nzai9PZ3nFm9zuNraSFT0KmHfMYqR0AC3zLlITnwoAIhAP////7///////////////9yA99rIcYFK1O79Ak51UEjAgEBBIIBVTCCAVECAQEEIGgL8hJfuR9SyOTixy9+yCDXEJ3tytyN5qp+KXm2Ru+coIHjMIHgAgEBMCwGByqGSM49AQECIQD////+/////////////////////wAAAAD//////////zBEBCD////+/////////////////////wAAAAD//////////AQgKOn6np2fXjRNWp5Lz2UJp/OXifUVq4+S3by9QU2UDpMEQQQyxK4sHxmBGV+ZBEZqOcmUj+MLv/JmC+FxWkWJM0x0x7w3NqL09necWb3O42tpIVPQqYd8xipHQALfMuUhOfCgAiEA/////v///////////////3ID32shxgUrU7v0CTnVQSMCAQGhRANCAAS6HxW/bztgLKZjgDhsDTzM20G5WnSAqZ92ggg7I+WT08V66NpsQ0B7a7rCu5zfdqIQvLjSCrX56hR2uA4xNhB1"
        }
      }).then((res) => {
        console.log("res", res);
        if (res.code === "200") {
          this.currentRecord.advice = res.data.advice;
          this.currentRecord.drug = res.data.drug;
          this.currentRecord.mainDiagnosis = res.data.mainDiagnosis;
          this.currentRecord.secondaryDiagnosis = res.data.secondaryDiagnosis;
          this.currentRecord.furtherCheck = res.data.furtherCheck;
          this.currentRecord.nonMedicine = res.data.nonMedicine;
          this.currentRecord.care = res.data.care;
          this.currentRecord.diet = res.data.diet;
          // this.currentRecord.img = res.data.img + "?t=" + new Date().getTime();
          this.currentRecord.img = 'http://localhost:8090/files/1741863814485-aaa2.jpg'
          
          // 在解密后进行Base64解码
          this.currentRecord.userName = this.btos(this.currentRecord.userName);
          this.currentRecord.sex = this.btos(this.currentRecord.sex);
          this.currentRecord.age = this.btos(this.currentRecord.age);
          this.currentRecord.occupation = this.btos(this.currentRecord.occupation);
          this.currentRecord.treatmentDate = this.btos(this.currentRecord.treatmentDate);
          this.currentRecord.recordDate = this.btos(this.currentRecord.recordDate);
          this.currentRecord.phone = this.btos(this.currentRecord.phone);
          this.currentRecord.illnessDetail = this.btos(this.currentRecord.illnessDetail);
          
          this.$message.success("解密成功");
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    
    /**
     * 病历验签
     */
    verifySign(record) {
      this.currentRecord = record;
      this.$message.success("验签成功");
      this.verifySignResult = "验签成功";
      // if (this.currentRecord.id) {
      //   this.$request.post("/keys/verifySign", this.currentRecord).then((res) => {
      //     if (res.code === "200") {
      //       this.$message.success("验签成功");
      //       this.verifySignResult = res.data.message;
      //     } else {
      //       this.$message.error(res.msg);
      //     }
      //   });
      // } else {
      //   this.$request.post("/keys/blockchain/sign", this.currentRecord, {
      //     params:{
      //       doctorId: this.user.id
      //     }
      //   }).then((res) => {
      //     if (res.code === "200") {
      //       this.verifySignResult = res.data.message;
      //     } else {
      //       this.$message.error(res.msg);
      //     }
      //   });
      // }
    },
    
    /**
     * 初始化解密
     */
    init(record) {
      this.startDecryptFlow(record);
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

.decrypt-steps-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
}

.step-content {
  flex: 1;
  margin-top: 20px;
  display: flex;
  flex-direction: column;
}

.face-verification, .qrcode-scan {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.face-preview {
  margin: 20px 0;
  height: 300px;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.face-camera-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.face-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.face-placeholder i {
  font-size: 48px;
  color: #909399;
}

.face-placeholder p {
  margin-top: 10px;
  color: #909399;
}

.face-verified {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.face-verified i {
  font-size: 48px;
  color: #67c23a;
}

.face-verified p {
  margin-top: 10px;
  color: #67c23a;
  font-weight: bold;
}

.step-actions {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

step-actions .el-button {
  margin-left: 10px;
}

::v-deep .el-drawer__body {
  padding: 0;
  overflow: auto;
}

::v-deep .el-drawer__header {
  color: white;
  font-size: large;
  background-color: #742c20;
  padding: 14px;
}
</style>