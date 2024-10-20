<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入账号查询" style="width: 200px" v-model="params.username"></el-input>
      <el-input placeholder="请输入姓名查询" style="width: 200px; margin-left: 5px" v-model="params.name"></el-input>
      <el-button type="primary" plain style="margin-left: 10px" @click="findBySearch">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      <el-button type="success" style="margin-left: 10px" plain @click="handleAdd">新增</el-button>
<!--      <el-button type="danger" plain @click="delBatch">批量删除</el-button>-->
    </div>


    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column label="头像">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.avatar"
                        :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="账号"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="description" label="护士介绍" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="管理病房"></el-table-column>
        <el-table-column prop="phone" label="电话" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮箱" show-overflow-tooltip></el-table-column>
        <el-table-column prop="role" label="角色"></el-table-column>
        <el-table-column prop="time" label="入职时间"></el-table-column>
        <el-table-column prop="position" label="职位"></el-table-column>
        <el-table-column prop="hospitalName" label="医院"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="paginat ion">
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
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Nurse",
  data(){
    return{
      params:{
        username:'',
        name:'',
      },
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 2,
    // 注意，分页查询的total还没写完！这是个假数据！
    }
  },
  //页面加载的时候，在created里做一些事情
  created(){
    this.load();
  },
  //页面上控件触发的方法
  methods:{
    load(){
      request.get("/nurse").then(res=>{
        if(res.code === '200'){
          this.tableData = res.data;
        }
        else{

        }
      })
    },
    findBySearch(){
      request.get("/nurse/research",{
        params : this.params
      }).then(res=>{
        if(res.code === '200'){
          this.tableData = res.data;
        }
        else{

        }
      })
    },
    reset(){
      this.params = {
        username:'',
        name:'',
      }
      this.findBySearch(); //不传任何参数就等于load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum;
      this.findBySearch();
    }
  }
}
</script>

<style>

</style>
