<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入转诊记录id" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="userName" label="患者" width="80px" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reason" label="转诊原因"></el-table-column>
        <el-table-column prop="result" label="结果" width="120px"></el-table-column>
        <el-table-column prop="outHospitalName" label="转出医院" show-overflow-tooltip></el-table-column>
        <el-table-column prop="outTime" label="转出时间" width="120px"></el-table-column>
        <el-table-column prop="inHospitalName" label="转入医院" show-overflow-tooltip></el-table-column>
        <el-table-column prop="inTime" label="转入时间" width="120px"></el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination background @current-change="handleCurrentChange" :current-page="pageNum"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReferralRecord",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      status: null,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),

    }
  },
  created() {
    this.load(1)
  },
  methods: {
    load(pageNum) {
      console.log(this.user)
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/referral/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
      }).then(res => {
        console.log("res", res)
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.status = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>
</style>
