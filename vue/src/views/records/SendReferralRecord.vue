<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入病历ID" style="width: 200px" v-model="id"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData"  stripe>
        <el-table-column prop="userDate" label="就诊日期" width="100" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="姓名" v-if="user.role === 'DOCTOR'" width="100" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" v-if="user.role === 'USER'" width="100" align="center"></el-table-column>
        <el-table-column prop="hospitalName" label="医院名称" v-if="user.role === 'USER'" width="100" align="center"></el-table-column>

        <el-table-column prop="inhospital" label="住院情况" width="200" align="center"></el-table-column>
        <el-table-column label="详情"  align="center">
          <template v-slot="scope">
            <el-button plain type="primary" size="mini" @click="goToCaseDetails(scope.row)">查看</el-button>
          </template>
        </el-table-column>
        <el-table-column label="发送"  align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" @click="sendTraverse(scope.row)">发送</el-button>
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
          name: this.user.role === 'DOCTOR' ? '' : this.user.name,
          id: this.id,
          //name: this.user.name
        }
      }).then(res => {
        this.tableData = res.data?.list
        for (let i = 0; i < this.tableData.length; i++) {
          this.tableData[i].userDate = this.tableData[i].userDate?.split('T')[0]
          // 将日期字符串转换为Date对象
          let date = new Date(this.tableData[i].userDate);
          // 增加一天（不知为何后端返回前端时间会减少一天？）
          date.setDate(date.getDate() + 1);
          // 将Date对象转换回日期字符串，如果需要保持相同的格式
          this.tableData[i].userDate = this.formatDate(date);
        }
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

    }
  }
}
</script>

<style scoped>

</style>
