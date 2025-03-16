<template>
  <div>
    <div class="search">
      <el-input
        placeholder="请输入关键词"
        style="width: 200px"
        v-model="keywords"
      ></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)"
        >查询</el-button
      >
      <el-button type="warning" plain style="margin-left: 10px" @click="reset"
        >重置</el-button
      >
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="userName" label="患者姓名"></el-table-column>
        <el-table-column
          prop="outDoctorName"
          label="转出医生"
          width="80px"
        ></el-table-column>
        <el-table-column prop="reason" label="转诊原因"></el-table-column>
        <el-table-column prop="outTime" label="转出时间"></el-table-column>
        <el-table-column
          prop="inHospitalName"
          label="转入医院"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column prop="referralStatus" label="结果"></el-table-column>
        <el-table-column
          label="操作"
          width="180"
          align="center"
          v-if="user.role === 'ADMIN'"
        >
          <template v-slot="scope">
            <el-button
              plain
              type="danger"
              size="mini"
              @click="update(scope.row)"
              :disabled="operation(scope.row)"
              >同意</el-button
            >
            <el-button
              plain
              type="danger"
              size="mini"
              @click="refuse(scope.row)"
              :disabled="operation(scope.row)"
              >拒绝</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          background
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total"
        >
        </el-pagination>
      </div>
    </div>

    <el-dialog
      :visible="showDialog"
      top="5%"
      width="60%"
      :title="dialog_title"
      center
      @close="handleClose"
    >
      <el-row>
        <div style="display: flex;justify-content: center; align-items: center; flex-direction: column;">
          <el-image :src="QR" fit="contain" style="width: 200px; height: 200px;"/>
          <p style="color: blue; font-size: 16px;text-align: center;">{{ blockAddr }}</p>
        </div>
      </el-row>

      <el-row>
        <h2 style="text-align: center">交易回执</h2>
        <CodeBlock :code="code" language="json" />
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import axois from "axios";
import CodeBlock from "../component/CodeBlock.vue";
export default {
  name: "ReferralRecord",
  components: {
    CodeBlock,
  },
  data() {

    return {
      tableData: [], // 所有的数据
      pageNum: 1, // 当前的页码
      pageSize: 10, // 每页显示的个数
      total: 0,
      keywords: null,
      user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
      showProgress: false,
      progressPercentage: 0,
      sendHospital: "",
      sendData: "",
      QR: "",
      blockAddr: "",
      dialog_title: "???",
      showDialog: false,
      code: "",
    };
  },
  created() {
    this.load(1);
  },
  methods: {
    operation(row) {
      return row.result === "待审批" ? false : true;
    },
    /**
     * 同意转出,自动上传转诊信息到区块链
     * @param row 转诊记录
     */
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

      // TODO:将转诊信息上传至区块链，获取到链上地址
      this.$confirm("同意审批将上传转诊信息至区块链，你确定要上传吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$blockRequest.post("/storeReferralInfo", referralInfo).then((res) => {
            if (res.data.code === "200") {
              this.$message("上传成功");
              // 得到本次转诊信息的hash值, 获取二维码
              this.showDialog = true;
              this.blockAddr = res.data.data.transactionReceipt.output
              this.init_code(res.data.data)
              this.$request
                .get("/files/generateQR", {
                  params: {
                    seed: this.blockAddr,
                  },
                })
                .then((res) => {
                  if (res.code === "200") {
                    this.QR = res.data;
                  }
                });
            }
          });
        })
        .catch(() => {
          this.$message("取消发送");
        });
    },
    /**
     * 传入要拒绝的转诊记录的id
     * @param row 待审批的转诊记录
     */
    refuse(row) {
      let form = {
        id: row.id,
      };
      this.$request.patch("/referral/refuseOut", form).then((res) => {
        if (res.code === "200") {
          // 表示成功保存
          this.$message.success("已拒绝接收");
          this.load(1);
          // this.record(row)
        } else {
          this.$message.error(res.msg); // 弹出错误的信息
        }
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
          this.tableData = res.data?.list;
          this.total = res.data?.total;
        });
    },
    reset() {
      this.status = null;
      this.load(1);
    },
    init_code(obj) {
      const { returnCode, returnMessage, transactionReceipt : {
        transactionHash,
        transactionIndex,
        root,
        blockNumber,
        blockHash,
        from,
        to,
        gasUsed,
        contractAddress,
        logs
      }} = obj;
      const transactionReceipt = {
        transactionHash,
        transactionIndex,
        blockHash: obj.transactionReceipt.output,
        root,
        blockNumber,
        from,
        to,
        gasUsed,
        contractAddress,
        logs
      };

      const res = {
        returnCode,
        returnMessage,
        transactionReceipt
      }
      this.code = JSON.stringify(res);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum);
    },
    /**
     * 显示上传病历至区块链的进度条
     * @param traverse 病历
     */
    startProgress(traverse) {
      var index = 0;
      const keys = Object.keys(traverse);
      this.progressPercentage = 0;
      let interval = setInterval(() => {
        if (this.progressPercentage >= 100) {
          clearInterval(interval);
        } else {
          this.progressPercentage += 10;
        }
        if (index <= keys.length) {
          this.sendData = keys[index] + " : " + traverse[keys[index]];
          index++;
        }
      }, 1000);

      // 模拟操作完成后，可以取消进度条显示
      setTimeout(() => {
        clearInterval(interval);
        this.showProgress = false;
        this.$message({
          message: "发送成功",
          type: "success",
        });
      }, 11000); // 假设操作需要5秒
    },
    handleClose() {
      this.showDialog = false;
    },
  },
};
</script>

<style scoped></style>
