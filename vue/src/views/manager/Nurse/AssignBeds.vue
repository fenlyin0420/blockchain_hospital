<!--<template>-->
<!--  <div>-->
<!--&lt;!&ndash;    <el-table&ndash;&gt;-->
<!--&lt;!&ndash;        :data="tableData"&ndash;&gt;-->
<!--&lt;!&ndash;        style="width: 100%">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column&ndash;&gt;-->
<!--&lt;!&ndash;          label="日期"&ndash;&gt;-->
<!--&lt;!&ndash;          width="180">&ndash;&gt;-->
<!--&lt;!&ndash;        <template slot-scope="scope">&ndash;&gt;-->
<!--&lt;!&ndash;          <i class="el-icon-time"></i>&ndash;&gt;-->
<!--&lt;!&ndash;          <span style="margin-left: 10px">{{ scope.row.date }}</span>&ndash;&gt;-->
<!--&lt;!&ndash;        </template>&ndash;&gt;-->

<!--&lt;!&ndash;      </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table-column label="病房A">&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;        <template slot-scope="scope">&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;          <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;&lt;!&ndash;        </template>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;      </el-table-column>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-table-column label="病房B">&ndash;&gt;-->
<!--&lt;!&ndash;        &lt;!&ndash;        <template slot-scope="scope">&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        &lt;!&ndash;        </template>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;      </el-table-column>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-table-column label="病房C">&ndash;&gt;-->
<!--&lt;!&ndash;        &lt;!&ndash;        <template slot-scope="scope">&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        &lt;!&ndash;        </template>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;      </el-table-column>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-table-column label="病房D">&ndash;&gt;-->
<!--&lt;!&ndash;        &lt;!&ndash;        <template slot-scope="scope">&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-button type="primary" size="mini" round>空位</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;        &lt;!&ndash;        </template>&ndash;&gt;&ndash;&gt;-->
<!--&lt;!&ndash;      </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-table>&ndash;&gt;-->


<!--&lt;!&ndash;    <div class="table" style="margin-top: 15px">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-table :data="tableDataRecord" strip @selection-change="handleSelectionChange">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column type="selection" align="center"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column prop="id" label="序号" align="center" sortable></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column prop="name" label="姓名"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column prop="doctorId" label="医生" show-overflow-tooltip></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column prop="advice" label="医嘱" show-overflow-tooltip></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column prop="drug" label="药品信息"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column prop="hospitalId" label="医院"></el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-table-column label="操作" align="center" width="180">&ndash;&gt;-->
<!--&lt;!&ndash;          <template v-slot="scope">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          </template>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-table>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->

<!--&lt;!&ndash;    <div class="paginat ion">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-pagination&ndash;&gt;-->
<!--&lt;!&ndash;          background&ndash;&gt;-->
<!--&lt;!&ndash;          @current-change="handleCurrentChange"&ndash;&gt;-->
<!--&lt;!&ndash;          :current-page="pageNum"&ndash;&gt;-->
<!--&lt;!&ndash;          :page-sizes="[5, 10, 20]"&ndash;&gt;-->
<!--&lt;!&ndash;          :page-size="pageSize"&ndash;&gt;-->
<!--&lt;!&ndash;          layout="total, prev, pager, next"&ndash;&gt;-->
<!--&lt;!&ndash;          :total="total">&ndash;&gt;-->
<!--&lt;!&ndash;      </el-pagination>&ndash;&gt;-->
<!--&lt;!&ndash;    </div>&ndash;&gt;-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import request from "@/utils/request";-->

<!--export default {-->
<!--  name: "AssignBeds",-->
<!--  data() {-->
<!--    return {-->
<!--      tableData: [{-->
<!--        date: '2016-05-02',-->
<!--        name: '王小虎',-->
<!--        address: 'balabala',-->
<!--        tag: '单人病房'-->
<!--      }, {-->
<!--        date: '2016-05-04',-->
<!--        name: '王小虎',-->
<!--        address: 'balabala',-->
<!--        tag: '双人病房'-->
<!--      }, {-->
<!--        date: '2016-05-01',-->
<!--        name: '王小虎',-->
<!--        address: 'balabala',-->
<!--        tag: '单人病房'-->
<!--      }, {-->
<!--        date: '2016-05-03',-->
<!--        name: '王小虎',-->
<!--        address: 'balabala',-->
<!--        tag: '双人病房'-->
<!--      }],-->
<!--      tableDataRecord:[],-->
<!--    }-->
<!--  },-->
<!--  created() {-->
<!--    this.load();-->
<!--  },-->
<!--  methods: {-->
<!--    load(){-->
<!--      request.get("/AssignBeds").then(res => {-->
<!--        if(res.code === '200'){-->
<!--          this.tableDataRecord = res.data;-->
<!--        }else{-->

<!--        }-->
<!--      })-->
<!--    },-->
<!--    handleSelectionChange(){-->

<!--    },-->

<!--  }-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->

<!--</style>-->