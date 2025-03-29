<template>
  <div>
    <!-- 标签栏 -->
    <div class="filter-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane name="all">
          <span slot="label" class="all-tab">
            全部转诊
            <el-badge v-if="allCount > 0" :value="allCount" class="all-badge"></el-badge>
          </span>
        </el-tab-pane>
        <el-tab-pane name="emergency">
          <span slot="label" class="emergency-tab">
            急诊转诊
            <el-badge
              v-if="emergencyCount > 0"
              :value="emergencyCount"
              class="emergency-badge"
            ></el-badge>
          </span>
        </el-tab-pane>
        <el-tab-pane name="normal">
          <span slot="label" class="normal-tab">
            普通转诊
            <el-badge
              v-if="normalCount > 0"
              :value="normalCount"
              class="normal-badge"
            ></el-badge>
          </span>
        </el-tab-pane>
        <el-tab-pane label="转诊接收" name="receive">
          <div class="qr-scan-container">
            <QRcodeScan
              :active="activeTab === 'receive'"
              @getPrivateKey="handleQRCodeData"
              title="获取转诊信息..."
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 转诊信息表单 -->
    <el-card v-if="ReferralRecords.length > 0 && activeTab !== 'receive'">
      <h3 style="text-align: center; margin-bottom: 10px">医院转诊申请表</h3>
      <el-form
        ref="elForm"
        :model="currentRecord"
        size="medium"
        label-width="auto"
        label-position="right"
      >
        <el-row :gutter="24">
          <el-col
            :span="12"
            style="border: 1px solid #ccc; border-radius: 10px; padding: 20px"
          >
            <el-row :gutter="24">
              <el-col :span="14">
                <el-form-item label="患者姓名">
                  <el-input v-model="currentRecord.userName" :readonly="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item class="custom-label" label="转诊类型">
                  <el-input
                    v-model="currentRecord.referralType"
                    :readonly="true"
                  ></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="9">
                <el-form-item label="年龄">
                  <el-input v-model="currentRecord.age" :readonly="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item class="custom-label" label="性别" label-width="50px">
                  <el-input v-model="currentRecord.sex" :readonly="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="身份证号码" label-width="90px">
                  <el-input v-model="currentRecord.idCard" :readonly="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="14">
                <el-form-item label="联系电话">
                  <el-input v-model="currentRecord.phone" :readonly="true"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item label="转诊编号">
                  <el-input v-model="currentRecord.id" :readonly="true"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="初步诊断">
              <el-input
                v-model="currentRecord.diagnosis"
                :readonly="true"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4 }"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>

            <el-form-item label="沟通记录">
              <el-input
                v-model="currentRecord.communication"
                :readonly="true"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>

            <el-form-item label="患者申请原因">
              <el-input
                v-model="currentRecord.reason"
                :readonly="true"
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4 }"
                :style="{ width: '100%' }"
              ></el-input>
            </el-form-item>

            <el-form-item label="患者签字">
              <el-image
                :src="currentRecord.signature"
                fit="contain"
                style="
                  width: 100%;
                  height: 100px;
                  border: 1px dashed #dcdfe6;
                  border-radius: 4px;
                "
              >
                <template #error>
                  <div class="image-slot">
                    <i
                      class="el-icon-picture-outline"
                      style="font-size: 30px; color: #909399"
                    ></i>
                    <p style="color: #909399; font-size: 14px; margin: 10px 0">
                      暂无签名
                    </p>
                  </div>
                </template>
                <template #placeholder>
                  <div class="image-slot">
                    <i class="el-icon-loading"></i>
                    <p style="color: #909399; font-size: 14px; margin: 10px 0">
                      加载中...
                    </p>
                  </div>
                </template>
              </el-image>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <div style="border: 1px solid #ccc; border-radius: 10px; padding: 20px">
              <el-row :gutter="24">
                <el-col :span="10">
                  <el-form-item label="转诊状态">
                    <el-input
                      v-model="currentRecord.referralStatus"
                      :readonly="true"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="14">
                  <el-form-item label="病历地址">
                    <el-input
                      v-model="currentRecord.traverseAddr"
                      :readonly="true"
                    ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="24">
                <el-col :span="13">
                  <el-form-item label="转出医院">
                    <el-input
                      v-model="currentRecord.outHospitalName"
                      :readonly="true"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="转出医生">
                    <el-input
                      v-model="currentRecord.outDoctorName"
                      :readonly="true"
                    ></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="转出日期">
                <el-input
                  v-model="currentRecord.outTime"
                  :readonly="true"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>

              <el-form-item label="医务科意见">
                <el-input
                  v-model="currentRecord.outHospitalAdvice"
                  :readonly="true"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </div>

            <br />
            <div style="border: 1px solid #ccc; border-radius: 10px; padding: 20px">
              <el-row :gutter="24">
                <el-col :span="13">
                  <el-form-item label="转入医院">
                    <el-input v-model="currentRecord.inHospitalName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="转入医生">
                    <el-input v-model="currentRecord.inDoctorName"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="转入日期">
                <el-date-picker
                  v-model="currentRecord.inTime"
                  type="date"
                  placeholder="选择日期"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  style="width: 100%"
                ></el-date-picker>
              </el-form-item>

              <el-form-item label="医务科意见">
                <el-input
                  v-model="currentRecord.inHospitalAdvice"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>

              <el-form-item label="医保经办机构意见">
                <el-input
                  v-model="currentRecord.globalAdvice"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <div class="button-container">
        <div class="operation-buttons" v-if="user.role === 'ADMIN'">
          <!-- <el-button type="success" @click="acceptReferral">同意接收</el-button>
                <el-button type="danger" @click="rejectReferral">拒绝接收</el-button> -->
          <el-button type="primary" @click="saveReferralDetail">保存修改</el-button>
          <el-button type="primary" @click="viewMedicalHistory(currentRecord)"
            >溯源病历</el-button
          >
        </div>

        <!-- 导航按钮组，始终显示 -->
        <div class="navigation-buttons">
          <el-button type="primary" :disabled="currentIndex === 0" @click="previousRecord"
            >上一个</el-button
          >
          <span>{{ currentIndex + 1 }} / {{ filteredTotal }}</span>
          <el-button
            type="primary"
            :disabled="currentIndex >= ReferralRecords.length - 1"
            @click="nextRecord"
            >下一个</el-button
          >
        </div>
      </div>
    </el-card>

    <el-empty v-else description="暂无转诊记录"></el-empty>
  </div>
</template>

<script>
import axios from "axios";
import QRcodeScan from "@/views/component/QRcodeScan.vue";

export default {
  name: "ReferralRecord",
  components: {
    QRcodeScan,
  },
  data() {
    return {
      ReferralRecords: [], // 所有的转诊信息
      pageNum: 1,
      pageSize: 10,
      total: 0,
      keywords: "",
      referralHash: "",
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      showReferralDetail: false,
      progressPercentage: 0,
      dialogTitle: "",
      blockInfo: [{ QR: "", blockHash: "NULL" }],
      opt: "溯源所有病历",
      placeholder: "请输入身份证号",
      idCard: "",
      activeTab: "all",
      emergencyCount: 0,
      normalCount: 0,
      allCount: 0,
      autoSaveInterval: null,
      currentRecord: {},
      currentIndex: 0,
      allData: [], // 存储所有未筛选的数据
      pollingInterval: null, // 轮询定时器
    };
  },
  computed: {
    filteredTotal() {
      return this.ReferralRecords.length;
    },
  },
  created() {
    // 先从localStorage加载数据
    this.loadDataFromLocalStorage();
    // 然后从服务器加载数据
    setTimeout(() => {
      this.load(1);
    }, 100);
    
    // 设置自动保存定时器 - 减少保存频率，防止性能问题
    this.autoSaveInterval = setInterval(() => {
      // 不需要自动保存，因为我们已经在获取新数据时保存数据
      localStorage.setItem("referralActiveTab", this.activeTab);
    }, 5000);
    
    // 设置轮询定时器，每30秒获取一次数据
    this.pollingInterval = setInterval(() => {
      this.load(1);
    }, 30000);
  },
  methods: {
    /**
     * 根据标签筛选数据
     * @param data 转诊信息列表
     * @param tab 标签类型
     * @returns 筛选后的转诊信息列表
     */
    filterDataByTab(data, tab) {
      // 首先过滤掉已处理过的转诊信息
      const activeData = data.filter(item => 
        item.referralStatus !== "已拒绝接收" && 
        item.referralStatus !== "已确认接收"
      );

      // 然后根据标签类型进行筛选
      if (tab === "normal") {
        return activeData.filter((item) => (item.referralType || "普通") === "普通");
      } else if (tab === "emergency") {
        return activeData.filter((item) => item.referralType === "急诊");
      }
      return activeData;
    },

    /**
     * 计算各种类型的数量
     * @param data 转诊信息列表
     */
    calculateCounts(data) {
      // 首先过滤掉已处理过的转诊信息
      const activeData = data.filter(item => 
        item.referralStatus !== "已拒绝接收" && 
        item.referralStatus !== "已确认接收"
      );

      this.emergencyCount = activeData.filter((item) => item.referralType === "急诊").length;
      this.normalCount = activeData.filter((item) => (item.referralType || "普通") === "普通").length;
      this.allCount = activeData.length;
    },

    /**
     * 解析转诊信息
     * @param data 区块链返回的转诊信息
     */
    parseReferralRecord(data) {
      // 如果data是字符串数组，则处理多个转诊信息
      if (Array.isArray(data)) {
        const referrals = [];
        // 按"-----------"分割多个转诊信息
        const referralStrings = data[0].split("-----------").filter(str => str.trim());
        
        for (const referralString of referralStrings) {
          const parsedObject = {};
          const lines = referralString.trim().split("\n");
          
          for (const line of lines) {
            const [key, value] = line.split(": ");
            if (key && value) {
              parsedObject[key] = value;
            }
          }
          
          if (Object.keys(parsedObject).length > 0) {
            referrals.push(this.convertToEnglishObjectReferral(parsedObject));
          }
        }
        return referrals;
      }
      
      // 如果data是单个转诊信息
      const parsedObject = {};
      const lines = data[0].trim().split("\n");
      
      for (const line of lines) {
        const [key, value] = line.split(": ");
        if (key && value) {
          parsedObject[key] = value;
        }
      }
      
      return [this.convertToEnglishObjectReferral(parsedObject)];
    },

    convertToEnglishObjectTraverse(chineseObj) {
      return Object.entries(chineseObj).reduce((acc, [key, value]) => {
        switch (key) {
          case "病例编号":
            acc.caseNumber = value;
            break;
          case "身份证号":
            acc.idCard = value;
            break;
          case "患者-医院-医生":
            acc.userName = value.split("||")[0];
            acc.hospitalName = value.split("||")[1];
            acc.doctorName = value.split("||")[2];
            break;
          case "时间戳-病情":
            acc.timestamp = value.split("||")[0];
            acc.illnessDetail = value.split("||")[1];
            break;
          case "就诊日期-记录日期":
            acc.treatmentDate = value.split("||")[0];
            acc.recordDate = value.split("||")[1];
            break;
          case "住院状态":
            acc.inHospital = value;
            break;
          case "药物医嘱":
            acc.drug = value.split("||")[0];
            acc.advice = value.split("||")[1];
            break;
          case "诊疗结果":
            acc.diagnosis = value;
            break;
          case "医疗影像":
            acc.img = value;
            break;
          case "数字签名":
            acc.signData = value;
            break;
          case "组织公钥":
            acc.signPubKey = value;
            break;
        }
        return acc;
      }, {});
    },

    convertToEnglishObjectReferral(chineseObj) {
      return Object.entries(chineseObj).reduce((acc, [key, value]) => {
        switch (key) {
          // 转诊信息
          case "医生意见":
            acc.outHospitalAdvice = value;
            break;
          case "姓名":
            acc.userName = value;
            break;
          case "年龄":
            acc.age = value;
            break;
          case "身份证号":
            acc.idCard = value;
            break;
          case "当前状态":
            acc.referralStatus = value;
            break;
          case "性别":
            acc.sex = value;
            break;
          case "患者签字":
            acc.signature = value;
            break;
          case "沟通记录":
            acc.communication = value;
            break;
          case "紧急程度":
            acc.referralType = value;
            break;
          case "联系电话":
            acc.phone = value;
            break;
          case "转出医院":
            acc.outHospitalName = value;
            break;
          case "转出医生":
            acc.outDoctorName = value;
            break;
          case "转入医院":
            acc.inHospitalName = value;
            break;
          case "转出时间":
            acc.outTime = value;
            break;
          case "转诊原因":
            acc.reason = value;
            break;
          case "诊断结果":
            acc.diagnosis = value;
            break;
          case "转诊编号":
            acc.id = value;
            break;
        }
        return acc;
      }, {});
    },

    loadDataFromLocalStorage() {
      const storedData = localStorage.getItem("ReferralRecords");
      if (storedData) {
        try {
          const allData = JSON.parse(storedData);
          const activeTab = localStorage.getItem("referralActiveTab");
          if (activeTab) {
            this.activeTab = activeTab;
          }
          
          // 更新allData
          this.allData = allData;
          
          // 计算各类型的数量
          this.calculateCounts(allData);
          
          // 根据当前标签过滤数据
          this.ReferralRecords = this.filterDataByTab(allData, this.activeTab);
          
          console.log("从localStorage加载数据:", this.ReferralRecords.length, "条记录");
          
          // 如果有数据，显示第一条
          if (this.ReferralRecords.length > 0) {
            this.currentIndex = 0;
            this.currentRecord = JSON.parse(JSON.stringify(this.ReferralRecords[0]));
          } else {
            this.currentRecord = {};
          }
        } catch (error) {
          console.error("解析本地存储数据错误:", error);
          this.ReferralRecords = [];
          this.allData = [];
          this.emergencyCount = 0;
          this.normalCount = 0;
          this.allCount = 0;
        }
      } else {
        this.ReferralRecords = [];
        this.allData = [];
        this.emergencyCount = 0;
        this.normalCount = 0;
        this.allCount = 0;
      }
    },

    saveDataToLocalStorage(newItems) {
      console.log("准备保存数据:", newItems);
      
      // 获取localStorage中现有的数据
      const existingData = localStorage.getItem("ReferralRecords");
      let currentData = [];
      
      if (existingData) {
        try {
          currentData = JSON.parse(existingData);
          console.log("已有数据条数:", currentData.length);
        } catch (error) {
          console.error("解析现有数据错误:", error);
          currentData = [];
        }
      } else {
        console.log("localStorage中没有现有数据");
      }

      // 如果没有新数据，直接保存当前标签
      if (!newItems || newItems.length === 0) {
        console.log("没有新数据需要保存");
        localStorage.setItem("referralActiveTab", this.activeTab);
        return;
      }
      
      // 确保newItems是数组
      const itemsToAdd = Array.isArray(newItems) ? newItems : [newItems];
      console.log("需要添加的数据条数:", itemsToAdd.length);
      
      // 合并数据，避免重复
      let addedCount = 0;
      let updatedCount = 0;
      
      itemsToAdd.forEach(newItem => {
        // 跳过没有id或undefined的项
        if (!newItem || !newItem.id) {
          console.log("跳过无效数据:", newItem);
          return;
        }
        
        console.log("处理数据项:", newItem.id);
        const existingIndex = currentData.findIndex(item => item && item.id === newItem.id);
        if (existingIndex === -1) {
          // 如果不存在，添加新记录
          currentData.push(newItem);
          addedCount++;
          console.log("添加新记录:", newItem.id);
        } else {
          // 如果已存在，更新记录
          currentData[existingIndex] = newItem;
          updatedCount++;
          console.log("更新现有记录:", newItem.id);
        }
      });

      // 保存合并后的数据
      localStorage.setItem("ReferralRecords", JSON.stringify(currentData));
      localStorage.setItem("referralActiveTab", this.activeTab);
      
      // 输出日志
      console.log("保存到localStorage:", currentData.length, "条记录 (新增:", addedCount, ", 更新:", updatedCount, ")");
      
      // 更新内存中的完整数据
      this.allData = currentData;
    },

    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$blockRequest
        .post("/getAllReferrals")
        .then((res) => {
          if (res.data.code === "200") {
            const referrals = this.parseReferralRecord(res.data.data.returnObject);
            // 过滤掉已处理过的转诊信息
            const filteredReferrals = referrals.filter(item => 
              item.referralStatus !== "已拒绝接收" && 
              item.referralStatus !== "已确认接收"
            );
            
            console.log("从服务器获取数据:", filteredReferrals.length, "条记录");
            
            // 保存新数据到localStorage
            this.saveDataToLocalStorage(filteredReferrals);
            
            // 根据当前标签过滤数据
            this.ReferralRecords = this.filterDataByTab(this.allData, this.activeTab);
            
            // 如果有数据，显示第一条
            if (this.ReferralRecords.length > 0) {
              this.currentIndex = 0;
              this.currentRecord = JSON.parse(JSON.stringify(this.ReferralRecords[0]));
            } else {
              // 如果没有数据，清空当前记录
              this.currentIndex = 0;
              this.currentRecord = {};
            }
          } else {
            this.$message.error("获取转诊信息失败: " + (res.data.msg || "未知错误"));
            this.loadDataFromLocalStorage();
          }
        })
        .catch((error) => {
          console.error("获取转诊信息错误:", error);
          this.$message.error("获取转诊信息失败: " + (error.message || "网络错误"));
          this.loadDataFromLocalStorage();
        });
    },

    handleQRCodeData(data) {
      this.referralHash = data;
      console.log("referralHash", data);
      this.pullReferralInfo();
    },

    pullReferralInfo() {
      if (!this.referralHash) {
        this.$message.warning("请先扫描转诊二维码");
        return;
      }

      this.$blockRequest.post("/getReferralInfoByTransactionHash", {
        _referralHash: this.referralHash,
      })
        .then((res) => {
          if (res.data.code === "200") {
            const newReferrals = this.parseReferralRecord(res.data.data.returnObject);
            console.log("扫描获取的转诊信息:", newReferrals);
            
            if (newReferrals.length > 0) {
              const newReferral = newReferrals[0]; // 只取第一个转诊信息
              console.log("处理的转诊信息:", newReferral);
              console.log("转诊ID:", newReferral.id);
              
              // 检查转诊状态，如果是已处理过的则提示并返回
              if (newReferral.referralStatus === "已拒绝接收" || 
                  newReferral.referralStatus === "已确认接收") {
                this.$message.warning("该转诊信息已处理完成");
                return;
              }

              // 如果没有id，为其指定一个临时id
              if (!newReferral.id) {
                newReferral.id = "temp_" + Date.now();
                console.log("为转诊信息指定临时ID:", newReferral.id);
              }

              // 保存新数据到localStorage
              this.saveDataToLocalStorage(newReferral);
              
              // 更新计数
              this.calculateCounts(this.allData);
              
              // 根据当前标签过滤数据
              this.ReferralRecords = this.filterDataByTab(this.allData, this.activeTab);
              
              // 找到新添加的记录的索引
              const newIndex = this.ReferralRecords.findIndex(item => item.id === newReferral.id);
              if (newIndex !== -1) {
                this.currentIndex = newIndex;
                this.currentRecord = JSON.parse(JSON.stringify(newReferral));
              } else {
                // 如果在当前标签中找不到，切换到全部标签
                this.activeTab = "all";
                this.ReferralRecords = this.filterDataByTab(this.allData, this.activeTab);
                const allIndex = this.ReferralRecords.findIndex(item => item.id === newReferral.id);
                if (allIndex !== -1) {
                  this.currentIndex = allIndex;
                  this.currentRecord = JSON.parse(JSON.stringify(newReferral));
                }
              }
            }
          } else {
            this.$message.error(res.data.msg || "获取转诊信息失败");
          }
        })
        .catch((error) => {
          console.error("获取转诊信息错误:", error);
          this.$message.error("获取转诊信息失败: " + (error.message || "网络错误"));
        });
    },

    acceptReferral() {
      if (!this.validateReferralForm()) {
        return;
      }

      const updateForm = {
        id: this.referralDetailForm.id,
        inHospitalName: this.referralDetailForm.inHospitalName,
        inDoctorName: this.referralDetailForm.inDoctorName,
        inTime: this.referralDetailForm.inTime,
        inHospitalAdvice: this.referralDetailForm.inHospitalAdvice,
        globalAdvice: this.referralDetailForm.globalAdvice,
      };

      this.$request.put("/referral/update", updateForm).then((updateRes) => {
        if (updateRes.code === "200") {
          this.$request
            .put("/referral/agreenIn", { id: this.referralDetailForm.id })
            .then((agreeRes) => {
              if (agreeRes.code === "200") {
                this.$message.success("已同意接收转诊并保存信息");
                this.showReferralDetail = false;
                this.load(1);
              } else {
                this.$message.error(agreeRes.msg);
              }
            });
        } else {
          this.$message.error(updateRes.msg);
        }
      });
    },

    validateReferralForm() {
      if (
        !this.referralDetailForm.inHospitalName ||
        !this.referralDetailForm.inDoctorName ||
        !this.referralDetailForm.inTime ||
        !this.referralDetailForm.inHospitalAdvice
      ) {
        this.$message.warning("请完善转入信息");
        return false;
      }
      return true;
    },

    rejectReferral() {
      this.$confirm("确定要拒绝接收该转诊申请吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$request
            .put("/referral/refuseIn", { id: this.referralDetailForm.id })
            .then((res) => {
              if (res.code === "200") {
                this.$message.success("已拒绝接收转诊");
                this.showReferralDetail = false;
                this.load(1);
              } else {
                this.$message.error(res.msg);
              }
            });
        })
        .catch(() => {
          this.$message.info("已取消操作");
        });
    },

    saveReferralDetail() {
      if (!this.validateReferralForm()) {
        return;
      }

      const form = {
        id: this.referralDetailForm.id,
        inHospitalName: this.referralDetailForm.inHospitalName,
        inDoctorName: this.referralDetailForm.inDoctorName,
        inTime: this.referralDetailForm.inTime,
        inHospitalAdvice: this.referralDetailForm.inHospitalAdvice,
        globalAdvice: this.referralDetailForm.globalAdvice,
      };

      this.$request
        .put("/referral/update", form)
        .then((res) => {
          if (res.code === "200") {
            this.$message.success("转诊信息保存成功");
            this.showReferralDetail = false;
            this.load(1);
          } else {
            this.$message.error(res.msg || "保存失败");
          }
        })
        .catch((error) => {
          console.error("保存转诊信息错误:", error);
          this.$message.error("保存失败: " + (error.message || "网络错误"));
        });
    },

    handleTabChange() {
      if (this.activeTab === "receive") {
        this.referralHash = "";
        return;
      }

      // 直接使用当前allData进行过滤，不需要重新从localStorage加载
      this.ReferralRecords = this.filterDataByTab(this.allData, this.activeTab);
      this.currentIndex = 0;
      if (this.ReferralRecords.length > 0) {
        this.currentRecord = JSON.parse(JSON.stringify(this.ReferralRecords[0]));
      } else {
        this.currentRecord = {};
      }
      localStorage.setItem("referralActiveTab", this.activeTab);
    },

    previousRecord() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
        this.currentRecord = JSON.parse(
          JSON.stringify(this.ReferralRecords[this.currentIndex])
        );
      }
    },

    nextRecord() {
      if (this.currentIndex < this.ReferralRecords.length - 1) {
        this.currentIndex++;
        this.currentRecord = JSON.parse(
          JSON.stringify(this.ReferralRecords[this.currentIndex])
        );
      }
    },

    viewMedicalHistory(row) {
      if (!row.idCard) {
        this.$message.warning("无法获取患者身份证号，无法溯源病历");
        return;
      }
      this.$router.push({
        name: "CaseHistory",
        query: { idCard: row.idCard },
      });
    },
  },
  beforeDestroy() {
    if (this.autoSaveInterval) {
      clearInterval(this.autoSaveInterval);
    }
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
    }
    
    // 页面销毁前保存当前activeTab
    localStorage.setItem("referralActiveTab", this.activeTab);
  },
};
</script>

<style scoped>
.search * {
  margin-right: 10px;
  display: inline-block;
}

.search label {
  margin-right: 5px;
}

.el-table {
  color: blue;
}
::v-deep .el-tabs__header {
  margin-bottom: 0px;
}
::v-deep .el-card__body {
  padding-top: 5px;
}
.verifyBtn {
  margin-top: 5px;
  width: 30%;
  margin-left: 70%;
}

.pullTraverse,
.pullTraverse * {
  margin: 10px;
}

.pullTraverse #input {
  width: fit-content;
}

/* 使用深度选择器确保样式能够穿透组件 */
.all-badge >>> .el-badge__content {
  background-color: #409eff;
  color: white;
  border: none;
}

.normal-badge >>> .el-badge__content {
  background-color: #67c23a;
  color: white;
  border: none;
}

.emergency-badge >>> .el-badge__content {
  background-color: #f56c6c;
  color: white;
  border: none;
}

.all-tab {
  color: #409eff;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.normal-tab {
  color: #67c23a;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.emergency-tab {
  color: #f56c6c;
  font-weight: bold;
  display: flex;
  align-items: center;
}

/* 确保badge在标签中正确显示 */
.el-tabs__item {
  height: auto;
  line-height: normal;
  padding: 10px 20px;
}

/* 添加新的样式 */
.button-container {
  display: flex;
  align-items: center;
  margin-top: 20px;
  width: 100%;
}

.operation-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
  width: 100%;
}

.operation-buttons .el-button {
  min-width: 100px;
}

.navigation-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  width: 100%;
}

.navigation-buttons span {
  min-width: 80px;
  text-align: center;
  font-size: 16px;
}

.navigation-buttons .el-button {
  min-width: 100px;
}

.el-form-item {
  margin-bottom: 18px;
}

.el-input.is-disabled .el-input__inner {
  color: #606266;
}

.el-textarea.is-disabled .el-textarea__inner {
  color: #606266;
}

.image-slot {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
}

.qr-scan-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 500px;
  background-color: #f5f7fa;
  border-radius: 8px;
  margin: 20px 0;
}
</style>
