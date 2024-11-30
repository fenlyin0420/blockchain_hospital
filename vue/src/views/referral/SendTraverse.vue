<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入病历ID" style="width: 200px" v-model="id"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData"  stripe>
        <el-table-column prop="treatmentDate" label="就诊日期" width="100" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="姓名" v-if="user.role === 'DOCTOR'" width="100" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名"  width="100" align="center"></el-table-column>
        <el-table-column prop="hospitalName" label="医院名称" width="200" align="center"></el-table-column>
        <el-table-column label="详情"  align="center">
          <template v-slot="scope">
            <el-button plain type="primary" size="mini" @click="goToCaseDetails(scope.row)">查看</el-button>
          </template>
        </el-table-column>
        <el-table-column label="发送"  align="center">
          <template v-slot="scope">
            <el-button plain type="danger"  size="mini" @click="sendTraverse(scope.row)">发送</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>

      <el-dialog :visible="showProgress" top="calc(100% / 4)" title="接收医院：xx大学第二附属医院" center>
        <el-progress v-if="showProgress" :percentage="progressPercentage" class="progress-demo"></el-progress>
        <div v-if="showProgress" style="font-size: 10px; left:0">正在发送: {{ sendData }}</div>
      </el-dialog>
            
    </div>
    <div>
      
    </div>
  </div>
</template>

<script>

export default {
  name: "SendReferralRecord",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      id: null,
      formVisible: false,
      form: {
        number: '',
        name: '',
        doctorName: '',
        hospitalName: '',
        jurisdiction: ''
      },
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      showProgress: false,
      progressPercentage: 0,
      sendHospital: '',
      sendData: '',
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/traverse/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          doctorId: this.user.role === 'DOCTOR' ? this.user.id : null,
        }
      }).then(res => {
        this.tableData = res.data?.list
        console.log(this.tableData)
        this.total = res.data?.total
      })
    },
    // 辅助函数，用于将Date对象格式化为您需要的字符串格式
    formatDate(date) {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      // 根据您的需要，这里可以是其他格式，例如 YYYY-MM-DD 等
      return `${year}-${month}-${day}`;
    },
    reset() {
      this.id = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    goToCaseDetails(row) {
      this.$router.push({
        name: 'CaseDetails',
        query: row
      });
    },
    sendTraverse(row) {
      this.$confirm('你确定要发送这份病历吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        this.sendHospital = row.hospitalName
        this.showProgress = true;
        this.startProgress(row);
      }).catch(() => {
        this.$message('取消发送')
      });
    },
    startProgress(row) {
      var index = 0
      const keys = Object.keys(row)
      this.progressPercentage = 0
        let interval = setInterval(() => {
          if (this.progressPercentage >= 100) {
            clearInterval(interval);
          } else {
            this.progressPercentage += 10;
          }
          if (index <= keys.length){
            this.sendData = keys[index] + ' : ' + row[keys[index]]
            index++ 
          }
        }, 1000);
  
        // 模拟操作完成后，可以取消进度条显示
        setTimeout(() => {
          clearInterval(interval);
          this.showProgress = false;
          this.$message({
            message: '发送成功',
            type: 'success',
          });
        }, 11000); // 假设操作需要5秒
    },
  }
}
</script>

<style scoped>
.progress-demo {
  width: 100%;
  margin-top: 10px;
  margin-bottom: 10px;
}

</style>
