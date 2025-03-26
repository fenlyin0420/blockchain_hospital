<template>
  <div>
    <el-card v-if="tableData.length > 0">
      <h1 style="text-align: center; margin-bottom: 10px">医院转诊申请表</h1>
      <el-form
        ref="elForm"
        :model="currentRecord"
        :rules="rules"
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
                  <el-select v-model="currentRecord.referralType" :readonly="currentRecord.referralStatus !== '待审批'">
                    <el-option label="普通" value="普通"></el-option>
                    <el-option label="急诊" value="急诊"></el-option>
                  </el-select>
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
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4 }"
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
                :src="currentRecord.signatureUrl"
                fit="contain"
                style="width: 100%; height: 100px; border: 1px dashed #dcdfe6; border-radius: 4px;"
              >
                <template #error>
                  <div class="image-slot">
                    <i class="el-icon-picture-outline" style="font-size: 30px; color: #909399;"></i>
                    <p style="color: #909399; font-size: 14px; margin: 10px 0;">暂无签名</p>
                  </div>
                </template>
                <template #placeholder>
                  <div class="image-slot">
                    <i class="el-icon-loading"></i>
                    <p style="color: #909399; font-size: 14px; margin: 10px 0;">加载中...</p>
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
                    <el-input v-model="currentRecord.referralStatus" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="14">
                  <el-form-item label="病历地址">
                    <el-input v-model="currentRecord.traverseAddr" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="24">
                <el-col :span="13">
                  <el-form-item label="转出医院">
                    <el-input v-model="currentRecord.outHospitalName" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="转出医生">
                    <el-input v-model="currentRecord.outDoctorName" :readonly="true"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="10">
                <el-col :span="10">
                  <el-form-item label="转出日期">
                    <el-input
                      v-model="currentRecord.outTime"
                      :style="{ width: '100%' }"
                    ></el-input>
                  </el-form-item>
                </el-col>

                <el-col :span="14">
                  <el-form-item label="拟转入医院">
                    <el-input
                      v-model="temp"
                      :style="{ width: '100%' }"
                    ></el-input>
                  </el-form-item>
                </el-col>

              </el-row>

              <el-form-item label="医务科意见">
                <el-input
                  v-model="currentRecord.outHospitalAdvice"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </div>

            <br /><br />
            <div style="border: 1px solid #ccc; border-radius: 10px; padding: 20px">
              <el-row :gutter="24">
                <el-col :span="13">
                  <el-form-item label="转入医院">
                    <el-input v-model="currentRecord.inHospitalName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="11">
                  <el-form-item label="转入医生">
                    <el-input v-model="currentRecord.inDoctorName" :readonly="true"></el-input>
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
                  :readonly="true"
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
        <div class="navigation-buttons" v-if="user.role === 'ADMIN'">
          <el-button type="success" @click="update(currentRecord)">同意</el-button>
          <el-button type="danger" @click="refuse(currentRecord)">拒绝</el-button>
          <el-button type="primary" @click="saveChanges">保存修改</el-button>
          <el-button type="primary" :disabled="currentIndex === 0" @click="previousRecord">上一个</el-button>
          <span>{{ currentIndex + 1 }} / {{ tableData.length }}</span>
          <el-button type="primary" :disabled="currentIndex >= tableData.length - 1" @click="nextRecord">下一个</el-button>
        </div>
      </el-form>
    </el-card>

    <el-empty v-else description="暂无转诊记录"></el-empty>

    <el-dialog
      title="转诊二维码"
      :visible.sync="qrCodeDialogVisible"
      width="400px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="true"
      center
    >
      <div class="qr-code-container">
        <el-image
          :src="qrCodeUrl"
          fit="contain"
          style="width: 300px; height: 300px;"
        >
          <template #error>
            <div class="image-slot">
              <i class="el-icon-picture-outline"></i>
              <p>二维码加载失败</p>
            </div>
          </template>
        </el-image>
        <p class="qr-code-tip">请使用手机扫描保存二维码</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "ReferralRecord",
  components: {
  },
  data() {
    return {
      tableData: [], // 所有的数据
      currentIndex: 0, // 当前显示的记录索引
      pageNum: 1,
      pageSize: 10,
      total: 0,
      keywords: null,
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      showProgress: false,
      progressPercentage: 0,
      sendHospital: "",
      sendData: "",
      blockAddr: "",
      dialog_title: "???",
      showQR: false,
      temp: "xx大学第二附属医院",
      qrCodeDialogVisible: false, // 二维码弹窗显示状态
      qrCodeUrl: '', // 二维码图片URL
      currentRecord: {
        outHospitalAdvice: undefined,
      },
      rules: {
        outHospitalAdvice: [
          {
            required: true,
            message: "请输入医务科意见",
            trigger: "blur",
          },
        ],
      }
    };
  },
  created() {
    this.load(1);
  },
  methods: {
    operation(row) {
      return row.referralStatus === "已转入";
    },
    previousRecord() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
        this.currentRecord = JSON.parse(JSON.stringify(this.tableData[this.currentIndex]));
      }
    },
    nextRecord() {
      if (this.currentIndex < this.tableData.length - 1) {
        this.currentIndex++;
        this.currentRecord = JSON.parse(JSON.stringify(this.tableData[this.currentIndex]));
      }
    },
    saveChanges() {
      // 保存修改后的数据
      const form = {
        id: this.currentRecord.id,
        outHospitalAdvice: this.currentRecord.outHospitalAdvice,
        referralType: this.currentRecord.referralType
      };

      this.$request.put("/referral/update", form).then((res) => {
        if (res.code === "200") {
          this.$message.success("修改成功");
          // 更新本地数据
          this.tableData[this.currentIndex] = JSON.parse(JSON.stringify(this.currentRecord));
        } else {
          this.$message.error(res.msg || "修改失败");
        }
      });
    },
    update(row) {
      this.dialog_title = row.userName + "的转诊信息"; 
      const referralInfo = {
        _userName: row.userName,
        _outHospitalName: row.outHospitalName,
        _inHospitalName: row.inHospitalName,
        _outTime: row.outTime,
        _reason: row.reason,
      };
      console.log(referralInfo);

      this.$confirm("同意审批将上传转诊信息至区块链，你确定要上传吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
            const referralInfo = {}
            let url = ""
            if (this.currentRecord.referralType === "急诊") {
              url = "/storeIntelReferralInfo"
            } else {
              url = "/storeReferralInfo"
              referralInfo.inHospitalName = this.currentRecord.inHospitalName
            }
            referralInfo.patientData = `${this.currentRecord.userName}||${this.currentRecord.sex}||${this.currentRecord.age}||${this.currentRecord.idCard}||${this.currentRecord.phone}`
            referralInfo.medicalData = `${this.currentRecord.diagnosis}||${this.currentRecord.reason}||${this.currentRecord.communication}||${this.currentRecord.signatureUrl}`
            referralInfo.outHospitalData = `${this.currentRecord.outHospitalName}||${this.currentRecord.outDoctorName}||${this.currentRecord.outHospitalAdvice}||${this.currentRecord.outTime}`
            referralInfo.status = '待接收'
            referralInfo.urgency = this.currentRecord.referralType            

            console.log("referralInfo", referralInfo)
            this.$blockRequest.post(url, referralInfo).then((blockRes) => {
              if (blockRes.data.code === "200") {
                this.$message.success("区块链数据上传成功");
                this.currentRecord.traverseAddr = blockRes.data.data.transactionReceipt.output
                console.log(blockRes)
                // 生成并显示二维码
                if (this.currentRecord.referralType !== "急诊") {
                  this.showQRCode(blockRes.data.data.transactionReceipt.output);
                }
              } else {
                this.$message.error("区块链数据上传失败: " + (blockRes.data.msg || "未知错误"));
              }
            }).catch(error => {
              console.error("区块链请求错误:", error);
              this.$message.error("区块链数据上传失败: " + (error.message || "网络错误"));
            });
        })
        .catch((error) => {
          console.error("取消发送:", error);
          this.$message("取消发送");
        });
    },
    refuse(row) {
      this.$confirm('确定要拒绝该转诊申请吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let form = {
          id: row.id,
        };
        this.$request.patch("/referral/refuseOut", form).then((res) => {
          if (res.code === "200") {
            this.$message.success("已拒绝转诊");
            this.load(1);
          } else {
            this.$message.error(res.msg);
          }
        });
      }).catch(() => {
        this.$message.info('已取消操作');
      });
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      // 查询本医院所有的转诊记录
      this.$request
        .get("/referral/selectPage", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
          },
        })
        .then((res) => {
          this.tableData = res.data?.list || [];
          this.total = res.data?.total || 0;
          
          // 如果有数据，显示第一条
          if (this.tableData.length > 0) {
            this.currentIndex = 0;
            this.currentRecord = JSON.parse(JSON.stringify(this.tableData[0]));
            this.currentRecord.outHospitalAdvice = '同意转诊，医保相关事宜已备案。'
            this.currentRecord.outTime = new Date().toISOString().split('T')[0]
          }
        });
    },
    reset() {
      this.keywords = null;
      this.load(1);
    },

    handleClose() {
      this.showDialog = false;
    },

    /**
     * 生成并显示二维码
     * @param {string} hash 区块哈希
     */
    async showQRCode(hash) {
      try {
        // 调用后端生成二维码
        const res = await this.$request.get('/files/generateQR', {
          params: {
            data: hash
          }
        });
        console.log(res)
        if (res.code === '200') {
          this.qrCodeUrl = res.data;
          this.qrCodeDialogVisible = true;
        } else {
          this.$message.error('生成二维码失败：' + (res.msg || '未知错误'));
        }
      } catch (error) {
        console.error('生成二维码错误:', error);
        this.$message.error('生成二维码失败：' + (error.message || '网络错误'));
      }
    },
  },
};
</script>

<style scoped>


.navigation-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.navigation-buttons span {
  margin: 0 20px;
  font-size: 16px;
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

.qr-code-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

.qr-code-tip {
  margin-top: 20px;
  color: #606266;
  font-size: 14px;
}

.image-slot i {
  font-size: 30px;
  margin-bottom: 10px;
}
</style>
