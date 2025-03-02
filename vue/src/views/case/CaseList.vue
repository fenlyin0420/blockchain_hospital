<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入患者姓名" style="width: 200px" v-model="userName"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="treatmentDate" label="就诊日期" width="200" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userName" label="姓名" v-if="user.role === 'DOCTOR'" width="100" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" v-if="user.role === 'USER'" width="100" align="center"></el-table-column>
        <el-table-column prop="diagnosis" label="诊断结果" align="center"></el-table-column>
        <el-table-column prop="drug" label="药品信息" align="center" width="200"></el-table-column>
        <el-table-column label="详情"  align="center">
          <template v-slot="scope">
            <el-button plain type="primary" size="mini" @click="goToCaseDetails(scope.row)">查看</el-button>
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
    </div>
  </div>
</template>

<script>

export default {
  name: "CaseList",
  data() {
    return {
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: null,
      userName:null, //患者姓名，用于查询
      id: null, //病历Id，用于查询（暂不使用）
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    /** 页面初始化，加载相关数据 */
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/traverse/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          doctorId: this.user.role === 'DOCTOR' ? this.user.id : null,
          userId: this.user.role === 'USER' ? this.user.id : null,
          userName: this.userName, //患者姓名，用于查询
          id:this.id, //病历Id，用于查询（暂不使用）
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
        console.log("888",this.tableData)
      })
    },
    // 辅助函数，用于将Date对象格式化为您需要的字符串格式
    formatDate(date) {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      // 根据需要，这里可以是其他格式，例如 YYYY-MM-DD 等
      return `${year}-${month}-${day}`;
    },
    reset() {
      this.userName =null
      this.id = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    goToCaseDetails(row) {
      console.log(row)
      this.$router.push({
        name: 'CaseDetail',
        query: row
      });
    },
  }
}
</script>

<style scoped>

</style>
