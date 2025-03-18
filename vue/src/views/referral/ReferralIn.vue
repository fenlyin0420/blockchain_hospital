<template>
  <div>
    <div class="search">
      <div class="update">
        <label for="pull">转诊信息:</label>
        <el-input
          id="pull"
          placeholder="输入转诊信息"
          style="width: 200px"
          v-model="referralInfo"
        ></el-input>
        <el-button type="primary" plain @click="pullReferralInfo">获取</el-button>
      </div>

      <div class="accept">
        <el-button type="primary" plain @click="autoPullReferralInfo"
          >自动拉取转诊信息【测试】</el-button
        >
      </div>
    </div>
    <!-- 添加标签筛选 -->
    <div class="filter-tabs">
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="全部转诊" name="all"></el-tab-pane>
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
        <el-tab-pane label="普通转诊" name="normal"></el-tab-pane>
      </el-tabs>
    </div>

    <!-- 转诊信息表单 -->
    <el-card v-if="tableData.length > 0">
      <h1 style="text-align: center; margin-bottom: 10px">医院转诊申请表</h1>
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
                    <el-input
                      v-model="currentRecord.inHospitalName"
                      :readonly="currentRecord.referralStatus !== '待审批'"
                    ></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="转入医生">
                    <el-input
                      v-model="currentRecord.inDoctorName"
                      :readonly="currentRecord.referralStatus !== '待审批'"
                    ></el-input>
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
                  :disabled="currentRecord.referralStatus !== '待接收'"
                ></el-date-picker>
              </el-form-item>

              <el-form-item label="医务科意见">
                <el-input
                  v-model="currentRecord.inHospitalAdvice"
                  :style="{ width: '100%' }"
                  :readonly="currentRecord.referralStatus !== '待接收'"
                ></el-input>
              </el-form-item>

              <el-form-item label="医保经办机构意见">
                <el-input
                  v-model="currentRecord.globalAdvice"
                  :style="{ width: '100%' }"
                  :readonly="currentRecord.referralStatus !== '待审批'"
                ></el-input>
              </el-form-item>
            </div>
          </el-col>
        </el-row>
      </el-form>
      <div class="button-container">
        <!-- 操作按钮组 -->
        <div
          class="operation-buttons"
          v-if="currentRecord.referralStatus === '待接收' && user.role === 'ADMIN'"
        >
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
            :disabled="currentIndex >= tableData.length - 1"
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

export default {
  name: "ReferralRecord",
  components: {},
  data() {
    return {
      tableData: [], // 所有的转诊信息
      pageNum: 1, // 当前的页码
      pageSize: 10, // 每页显示的个数
      total: 0,
      keywords: "",
      referralInfo: "",
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      showReferralDetail: false,
      showVerifySign: false,
      progressPercentage: 0,
      recievedData: "",
      signData: "1",
      signKey: "2",
      signResult: "3",
      dialogTitle: "",
      pubs: [],
      blockInfo: [{ QR: "", blockHash: "NULL" }],
      opt: "溯源所有病历",
      placeholder: "请输入身份证号",
      ReferralRecord: [],
      idCard: "",
      referralDetailForm: {
        userName: "",
        outHospitalName: "",
        outTime: "",
        reason: "",
        inHospitalName: "",
      },
      activeTab: "all", // 当前激活的标签
      emergencyCount: 0, // 急诊数量
      autoSaveInterval: null, // 自动保存定时器
      currentRecord: {},
      currentIndex: 0,
      allData: [], // 存储所有未筛选的数据
    };
  },
  computed: {
    // 根据当前标签计算过滤后的总数
    filteredTotal() {
      if (this.activeTab === "normal") {
        return this.allData.filter((item) => (item.referralType || "普通") === "普通")
          .length;
      } else if (this.activeTab === "emergency") {
        return this.allData.filter((item) => item.referralType === "急诊").length;
      }
      return this.allData.length;
    },
  },
  created() {
    // 从 localStorage 加载数据
    this.loadDataFromLocalStorage();

    // 加载数据
    this.load(1);

    // 设置自动保存定时器，每分钟保存一次
    this.autoSaveInterval = setInterval(() => {
      this.saveDataToLocalStorage();
    }, 60000);
  },
  methods: {
    /**
     * 从 localStorage 加载数据
     */
    loadDataFromLocalStorage() {
      // 加载表格数据
      const storedData = localStorage.getItem("referralTableData");
      if (storedData) {
        try {
          const allData = JSON.parse(storedData);

          // 恢复当前标签
          const activeTab = localStorage.getItem("referralActiveTab");
          if (activeTab) {
            this.activeTab = activeTab;
          }

          // 计算急诊数量
          this.emergencyCount = allData.filter(
            (item) => item.referralType === "急诊"
          ).length;

          // 根据当前标签筛选数据
          if (this.activeTab === "normal") {
            this.tableData = allData.filter(
              (item) => (item.referralType || "普通") === "普通"
            );
          } else if (this.activeTab === "emergency") {
            this.tableData = allData.filter((item) => item.referralType === "急诊");
          } else {
            this.tableData = allData;
          }
        } catch (error) {
          console.error("解析本地存储数据错误:", error);
          // 如果解析失败，设置空数组
          this.tableData = [];
          this.emergencyCount = 0;
        }
      } else {
        // 如果没有本地存储数据，设置空数组
        this.tableData = [];
        this.emergencyCount = 0;
      }

      // 加载转诊记录
      const storedReferralRecord = localStorage.getItem("ReferralRecord");
      if (storedReferralRecord) {
        try {
          this.ReferralRecord = JSON.parse(storedReferralRecord);
        } catch (error) {
          console.error("解析本地存储转诊记录错误:", error);
          this.ReferralRecord = {};
        }
      }
    },

    /**
     * 保存数据到 localStorage
     * @param {Array} allData 所有未筛选的数据，如果提供则直接使用，否则使用当前筛选后的数据
     */
    saveDataToLocalStorage(allData) {
      // 保存表格数据
      if (allData) {
        localStorage.setItem("referralTableData", JSON.stringify(allData));
      } else {
        // 如果没有提供完整数据，则保存当前筛选后的数据
        localStorage.setItem("referralTableData", JSON.stringify(this.tableData));
      }

      // 保存当前标签
      localStorage.setItem("referralActiveTab", this.activeTab);

      // 保存转诊记录
      if (this.ReferralRecord && Object.keys(this.ReferralRecord).length > 0) {
        localStorage.setItem("ReferralRecord", JSON.stringify(this.ReferralRecord));
      }
    },
    operation(row) {
      return row.referralStatus === "已转入";
    },
    /**
     * 同意转出
     * @param row 转诊记录
     */
    update(row) {
      let form = {
        id: row.id,
      };
      this.$request.put("/referral/agreenIn", form).then((res) => {
        if (res.code === "200") {
          // 表示成功保存
          this.load(1);
          this.record(row);
        } else {
          this.$message.error(res.msg); // 弹出错误的信息
        }
      });
    },
    refuse(row) {
      let form = {
        id: row.id,
      };
      this.$request.put("/referral/refuseIn", form).then((res) => {
        if (res.code === "200") {
          // 表示成功保存
          this.$message.success("拒绝接收");
          this.load(1);
          // this.record(row)
        } else {
          this.$message.error(res.msg); // 弹出错误的信息
        }
      });
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      // 从区块链获取所有转诊信息，而不是从数据库
      this.$blockRequest
        .post("/getAllReferrals")
        .then((res) => {
          if (res.data.code === "200") {
            // 解析转诊信息
            const referrals = this.parseReferrals(res.data.data.returnObject);
            this.allData = referrals; // 保存所有数据

            // 计算急诊数量
            this.emergencyCount = referrals.filter(
              (item) => item.referralType === "急诊"
            ).length;

            // 保存完整数据到 localStorage
            this.saveDataToLocalStorage(referrals);

            // 前端筛选数据
            if (this.activeTab === "normal") {
              this.tableData = referrals.filter(
                (item) => (item.referralType || "普通") === "普通"
              );
            } else if (this.activeTab === "emergency") {
              this.tableData = referrals.filter((item) => item.referralType === "急诊");
            } else {
              this.tableData = referrals;
            }

            // 如果有数据，显示第一条
            if (this.tableData.length > 0) {
              this.currentIndex = 0;
              this.currentRecord = JSON.parse(JSON.stringify(this.tableData[0]));
            }
          } else {
            this.$message.error("获取转诊信息失败: " + (res.data.msg || "未知错误"));
            // 如果获取失败，尝试从localStorage获取数据
            this.loadDataFromLocalStorage();
          }
        })
        .catch((error) => {
          console.error("获取转诊信息错误:", error);
          this.$message.error("获取转诊信息失败: " + (error.message || "网络错误"));

          // 如果加载失败，尝试从localStorage获取数据
          this.loadDataFromLocalStorage();
        });
    },
    reset() {
      this.status = null;
      this.load(1);
    },
    /**
     * 根据给定地址，从区块链获取对应病历
     */
    pullReferralInfo() {
      // this.showDialog = true;
      // this.showProgress = true;
      const Request = axios.create({
        baseURL: "http://localhost:8088", // 区块链管理平台的 baseURL
        timeout: 50000,
      });

      // 1. 根据转诊hash获取转诊信息,并存储转诊信息
      Request.post("/getReferralInfoByTransactionHash", {
        _transactionHash: this.referralInfo,
      }).then((res) => {
        if (res.data.code === "200") {
          // 解析数据，打开浮窗，准备拉取病历
          this.referralDetailForm = this.parseReferralRecord(res.data.data.returnObject);
          this.ReferralRecord = this.referralDetailForm;

          // 保存到本地存储
          localStorage.setItem("ReferralRecord", JSON.stringify(this.ReferralRecord));

          this.showReferralDetail = true;
        }
      });
    },
    // 页面轮询地执行该函数
    autoPullReferralInfo() {
      this.$blockRequest
        .post("/getAllReferrals")
        .then((res) => {
          if (res.data.code === "200") {
            console.log(res.data);

            // 解析转诊信息
            const referrals = this.parseReferrals(res.data.data.returnObject);
            console.log("解析后的转诊信息:", referrals);

            // 计算急诊数量
            this.emergencyCount = referrals.filter(
              (item) => item.referralType === "急诊"
            ).length;

            // 根据当前标签筛选数据
            if (this.activeTab === "normal") {
              this.tableData = referrals.filter(
                (item) => (item.referralType || "普通") === "普通"
              );
            } else if (this.activeTab === "emergency") {
              this.tableData = referrals.filter((item) => item.referralType === "急诊");
            } else {
              this.tableData = referrals;
            }

            // 保存到本地存储，传入完整数据
            this.saveDataToLocalStorage(referrals);

            this.$message.success(`成功获取 ${referrals.length} 条转诊信息`);
          } else {
            this.$message.error("获取转诊信息失败: " + (res.data.msg || "未知错误"));
          }
        })
        .catch((error) => {
          console.error("获取转诊信息错误:", error);
          this.$message.error("获取转诊信息失败: " + (error.message || "网络错误"));
        });
    },
    /**
     * 同意转诊，存储转诊信息
     */
    acceptReferral() {
      // 先验证表单是否填写完整
      if (
        !this.referralDetailForm.inHospitalName ||
        !this.referralDetailForm.inDoctorName ||
        !this.referralDetailForm.inTime ||
        !this.referralDetailForm.inHospitalAdvice
      ) {
        this.$message.warning("请先完善转入信息");
        return;
      }

      // 先保存表单信息
      let updateForm = {
        id: this.referralDetailForm.id,
        inHospitalName: this.referralDetailForm.inHospitalName,
        inDoctorName: this.referralDetailForm.inDoctorName,
        inTime: this.referralDetailForm.inTime,
        inHospitalAdvice: this.referralDetailForm.inHospitalAdvice,
        globalAdvice: this.referralDetailForm.globalAdvice,
      };

      this.$request.put("/referral/update", updateForm).then((updateRes) => {
        if (updateRes.code === "200") {
          // 保存成功后，执行同意操作
          let agreeForm = {
            id: this.referralDetailForm.id,
          };

          this.$request.put("/referral/agreenIn", agreeForm).then((agreeRes) => {
            if (agreeRes.code === "200") {
              this.$message.success("已同意接收转诊并保存信息");
              this.showReferralDetail = false;
              this.load(1); // 这里会调用 saveDataToLocalStorage 并传入完整数据
            } else {
              this.$message.error(agreeRes.msg);
            }
          });
        } else {
          this.$message.error(updateRes.msg);
        }
      });
    },
    detail(row) {
      // 复制一份数据，避免直接修改原始数据
      this.referralDetailForm = JSON.parse(JSON.stringify(row));

      // 设置默认值
      if (!this.referralDetailForm.inTime) {
        this.referralDetailForm.inTime = new Date().toISOString().split("T")[0];
      }

      if (!this.referralDetailForm.inHospitalName) {
        // 设置当前医院为转入医院
        this.referralDetailForm.inHospitalName = this.user.hospitalName || "";
      }

      if (!this.referralDetailForm.inDoctorName) {
        // 设置当前用户为转入医生
        this.referralDetailForm.inDoctorName = this.user.name || "";
      }

      if (!this.referralDetailForm.inHospitalAdvice) {
        this.referralDetailForm.inHospitalAdvice = "同意接收";
      }

      // 打开弹窗
      this.showReferralDetail = true;
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    handleRadioChange(label) {
      if (label === "溯源指定病历") this.placeholder = "请输入转诊hash";
      else this.placeholder = "请输入身份证号";
    },
    handleClose() {
      this.showReferralDetail = false;
    },
    parseReferralRecord(dataList) {
      const parsedObject = {};
      // 去除多余换行符并按换行符分割字符串
      const lines = dataList[0].trim().split("\n");
      for (const line of lines) {
        // 按冒号分割键值对
        const [key, value] = line.split(": ");
        if (key && value) {
          parsedObject[key] = value;
        }
      }
      return this.convertToEnglishObject(parsedObject);
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
            lines.forEach((line) => {
              const colonIndex = line.indexOf(":");
              if (colonIndex !== -1) {
                const key = line.slice(0, colonIndex).trim();
                const value = line.slice(colonIndex + 1).trim();
                obj[key] = value;
              }
            });
            return this.convertToEnglishObject(obj);
          });
      });
    },
    convertToEnglishObject(chineseObj) {
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

          // referralInfo convert
          case "转出医院":
            acc.outHospitalName = value;
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
        }
        return acc;
      }, {});
    },
    // 解析转诊信息字符串数组
    parseReferrals(dataArray) {
      if (!dataArray || !dataArray.length) return [];

      // 将所有数据合并为一个字符串
      const allData = dataArray.join("\n");

      // 按分隔符分割成单独的转诊记录
      const referralStrings = allData.split("-----------").filter((str) => str.trim());

      // 解析每条转诊记录
      return referralStrings.map((referralString) => {
        const referral = {};

        // 按行分割
        const lines = referralString.trim().split("\n");

        // 当前处理的部分（患者信息、医疗数据、转出信息）
        let currentSection = "";

        lines.forEach((line) => {
          line = line.trim();
          if (!line) return;

          // 检查是否是部分标题
          if (line.includes("【患者信息】")) {
            currentSection = "patient";
            return;
          } else if (line.includes("【医疗数据】")) {
            currentSection = "medical";
            return;
          } else if (line.includes("【转出信息】")) {
            currentSection = "referral";
            return;
          }

          // 解析键值对
          const colonIndex = line.indexOf(":");
          if (colonIndex > 0) {
            const key = line.substring(0, colonIndex).trim();
            const value = line.substring(colonIndex + 1).trim();

            // 根据不同部分和键名设置对象属性
            switch (key) {
              case "转诊编号":
                referral.id = value;
                break;
              case "姓名":
                referral.userName = value;
                break;
              case "性别":
                referral.sex = value;
                break;
              case "年龄":
                referral.age = value;
                break;
              case "身份证号":
                referral.idCard = value;
                break;
              case "联系电话":
                referral.phone = value;
                break;
              case "诊断结果":
                referral.diagnosis = value;
                break;
              case "转诊原因":
                referral.reason = value;
                break;
              case "沟通记录":
                referral.communication = value;
                break;
              case "患者签字":
                referral.signature = value;
                break;
              case "转出医院":
                referral.outHospitalName = value;
                break;
              case "转出医生":
                referral.outDoctorName = value;
                break;
              case "医生意见":
                referral.outHospitalAdvice = value;
                break;
              case "转出时间":
                referral.outTime = value;
                break;
              case "转入医院":
                referral.inHospitalName = value;
                break;
              case "当前状态":
                referral.referralStatus = value;
                break;
              case "紧急程度":
                referral.referralType = value;
                break;
            }
          }
        });

        return referral;
      });
    },
    // 拒绝转诊
    rejectReferral() {
      this.$confirm("确定要拒绝接收该转诊申请吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let form = {
            id: this.referralDetailForm.id,
          };
          this.$request.put("/referral/refuseIn", form).then((res) => {
            if (res.code === "200") {
              this.$message.success("已拒绝接收转诊");
              this.showReferralDetail = false;
              this.load(1); // 这里会调用 saveDataToLocalStorage 并传入完整数据
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
      // 验证必填字段
      if (
        !this.referralDetailForm.inHospitalName ||
        !this.referralDetailForm.inDoctorName ||
        !this.referralDetailForm.inTime ||
        !this.referralDetailForm.inHospitalAdvice
      ) {
        this.$message.warning("请完善转入信息");
        return;
      }

      let form = {
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
            this.load(1); // 这里会调用 saveDataToLocalStorage 并传入完整数据
          } else {
            this.$message.error(res.msg || "保存失败");
          }
        })
        .catch((error) => {
          console.error("保存转诊信息错误:", error);
          this.$message.error("保存失败: " + (error.message || "网络错误"));
        });
    },
    /**
     * 查看病历历史记录
     * @param row 当前行数据
     */
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
    /**
     * 处理标签切换
     */
    handleTabChange() {
      // 从localStorage获取完整数据
      const storedData = localStorage.getItem("referralTableData");
      if (storedData) {
        try {
          this.allData = JSON.parse(storedData);

          // 计算急诊数量（确保在切换标签时更新）
          this.emergencyCount = this.allData.filter(
            (item) => item.referralType === "急诊"
          ).length;

          // 根据当前标签筛选数据
          if (this.activeTab === "normal") {
            this.tableData = this.allData.filter(
              (item) => (item.referralType || "普通") === "普通"
            );
          } else if (this.activeTab === "emergency") {
            this.tableData = this.allData.filter((item) => item.referralType === "急诊");
          } else {
            this.tableData = this.allData;
          }

          // 重置当前记录索引并更新当前记录
          this.currentIndex = 0;
          if (this.tableData.length > 0) {
            this.currentRecord = JSON.parse(JSON.stringify(this.tableData[0]));
          }

          // 保存当前标签到 localStorage
          localStorage.setItem("referralActiveTab", this.activeTab);
        } catch (error) {
          console.error("解析本地存储数据错误:", error);
          // 如果解析失败，重新加载数据
          this.load(1);
        }
      } else {
        // 如果没有本地存储数据，重新加载数据
        this.load(1);
      }
    },
    /**
     * 计算急诊数量
     */
    countEmergencyReferrals() {
      // 已在 load 方法中计算，不需要单独请求
    },

    /**
     * 前端计算急诊数量（备用方案）
     */
    calculateEmergencyCount() {
      // 已在 load 方法中计算，不需要单独请求
    },
    /**
     * 上一条记录
     */
    previousRecord() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
        this.currentRecord = JSON.parse(
          JSON.stringify(this.tableData[this.currentIndex])
        );
      }
    },
    /**
     * 下一条记录
     */
    nextRecord() {
      if (this.currentIndex < this.tableData.length - 1) {
        this.currentIndex++;
        this.currentRecord = JSON.parse(
          JSON.stringify(this.tableData[this.currentIndex])
        );
      }
    },
  },
  // 添加 beforeDestroy 钩子，确保在组件销毁前保存数据
  beforeDestroy() {
    // 清除自动保存定时器
    if (this.autoSaveInterval) {
      clearInterval(this.autoSaveInterval);
    }

    // 保存数据
    this.saveDataToLocalStorage();
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

/* 添加标签样式 */
.filter-tabs {
  /* margin-bottom: 20px; */
} 

/* 使用深度选择器确保样式能够穿透组件 */
.emergency-badge >>> .el-badge__content {
  background-color: #f56c6c;
  color: white;
  border: none;
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

.el-card {
  margin-top: 20px;
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
</style>
