<template>
  <div>
    <el-table
        :data="tableData"
        style="width: 100%">
      <el-table-column label="病房A">
<!--        <template slot-scope="scope">-->
          <el-button type="primary" size="mini" round>空位</el-button>
<!--        </template>-->
      </el-table-column>

      <el-table-column label="病房B">
        <!--        <template slot-scope="scope">-->
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <!--        </template>-->
      </el-table-column>

      <el-table-column label="病房C">
        <!--        <template slot-scope="scope">-->
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <!--        </template>-->
      </el-table-column>

      <el-table-column label="病房D">
        <!--        <template slot-scope="scope">-->
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <el-button type="primary" size="mini" round>空位</el-button>
        <!--        </template>-->
      </el-table-column>
    </el-table>


    <div class="table" style="margin-top: 15px">
      <el-table :data="tableDataRecord" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="doctorId" label="医生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="advice" label="医嘱" show-overflow-tooltip></el-table-column>
        <el-table-column prop="hospitalId" label="医院名称"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

<!--    分页查询   -->
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
  name: "AssignBeds",
  data() {
    return {
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: 'balabala',
        tag: '单人病房'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: 'balabala',
        tag: '双人病房'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: 'balabala',
        tag: '单人病房'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: 'balabala',
        tag: '双人病房'
      }],
      tableDataRecord:[],
    }
  },
  created() {
    this.load();
  },
  methods: {
    load(){
      request.get("/AssignBeds/search").then(res => {
        if(res.code === '200'){
          this.tableDataRecord = res.data;
        }else{

        }
      })
    },
    handleSelectionChange(){

    },
//
  }
}
</script>

<style scoped>

</style>