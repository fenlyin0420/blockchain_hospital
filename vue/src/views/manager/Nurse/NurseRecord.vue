<template>
  <div>
    <div class="search">
      <!-- 选择病房-->
      <el-select v-model="wardName" style="width: 300px" placeholder="请选择病房号">
        <el-option label="重症监护室" value="重症监护室"></el-option>
        <el-option label="A号房" value="A号房"></el-option>
        <el-option label="B号房" value="B号房"></el-option>
        <el-option label="C号房" value="C号房"></el-option>
      </el-select>

      <!--  输入姓名   -->
      <el-input v-model="input" style="width: 300px; margin-left: 10px" placeholder="请输入姓名"></el-input>
      <el-button type="primary" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="success" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>
    <div class="table" style="margin-top: 15px">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column prop="traverse.id" label="序号" align="center" width="80px" sortable></el-table-column>
        <el-table-column prop="userName" label="姓名" align="center" width="80px"></el-table-column>
        <el-table-column prop="bedName" label="分配病床" align="center" width="180px">
          <template v-slot="scope">
            {{ scope.row.wardName }}-{{ scope.row.bedName }}
          </template>
        </el-table-column>
        <el-table-column prop="doctorName" label="主治医生" show-overflow-tooltip align="center" width="80px"></el-table-column>
        <el-table-column prop="traverse.mainDiagnosis" label="主要诊断" show-overflow-tooltip align="center"></el-table-column>
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
import request from "@/utils/request";

export default {
  name: "NurseRecord",
  data() {
    return {
      pageNum: 1,
      pageSize: 10,
      total: 0,
      input: '',
      tableData: [],
      wardName: null,  //用于查询
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    this.load(1); //查询病例
  },
  methods: {
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum;
      this.$request.get('/DailyCare/selectPageRecord',
        {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            hospitalId: this.user.hospitalId
          }
        },).then(res => {
          if (res.code === '200') {
            this.tableData = res.data?.list;
            this.total = res.data?.total;
            console.log("333",this.tableData)
          } else {
            this.$message.error(res.msg)
          }
        })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    reset() {

    },
    handleSelectionChange() { },
  }
}
</script>


<style scoped></style>