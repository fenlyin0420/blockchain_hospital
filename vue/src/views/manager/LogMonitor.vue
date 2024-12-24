<template>
  <div>

    <div class="search">
      <!--  输入姓名   -->
      <el-input placeholder="请输入操作人姓名" style="width: 200px" v-model="name"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table" style="margin-top: 15px">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>
        <el-table-column prop="content" label="操作内容" align="center"></el-table-column>
        <el-table-column prop="name" label="操作人" align="center"></el-table-column>
        <el-table-column prop="role" label="角色" align="center"></el-table-column>
        <el-table-column prop="time" label="操作时间" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="ip" label="ip" show-overflow-tooltip align="center"></el-table-column>
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
import request from "@/utils/request";

export default {
  name: "LogMonitor",
  data() {
    return {
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      name: null,
      tableData: [],
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1); //查询病例
  },
  methods: {
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/log/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          id:this.id,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.name = null;
      this.load(1);
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>


<style scoped>
.pagination{
  margin-top: 10px;
}
</style>