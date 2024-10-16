<template>
  <div>
    <el-form :inline="true" :model="searchForm" class="demo-form-inline" style="margin-top: 20px">
      <el-form-item label="姓名">
        <el-input v-model="searchForm.name" placeholder="姓名"></el-input>
      </el-form-item>
      <el-form-item label="病房号">
        <el-select v-model="searchForm.region" placeholder="病房号">
          <el-option label="病房A" value="1"></el-option>
          <el-option label="病房B" value="2"></el-option>
          <el-option label="病房C" value="3"></el-option>
          <el-option label="病房D" value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table
        ref="filterTable"
        :data="tableData"
        style="width: 100%">
      <el-table-column
          prop="date"
          label="日期"
          sortable
          column-key="date"
          :filters="[{text: '2016-05-01', value: '2016-05-01'}, {text: '2016-05-02', value: '2016-05-02'}, {text: '2016-05-03', value: '2016-05-03'}, {text: '2016-05-04', value: '2016-05-04'}]"
          :filter-method="filterHandler"
      >
      </el-table-column>
      <el-table-column
          prop="name"
          label="姓名">
      </el-table-column>
      <el-table-column
          prop="doctorName"
          label="医生姓名">
      </el-table-column>
      <el-table-column
          prop="address"
          label="处方"
          :formatter="formatter">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="200">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="primary" size="small">查看</el-button>
          <el-button type="danger" size="small">编辑</el-button>
        </template>
      </el-table-column>
      <el-table-column
          prop="tag"
          label="标签"
          :filters="[{ text: '单人病房', value: '单人病房' }, { text: '双人病房', value: '双人病房' }]"
          :filter-method="filterTag"
          filter-placement="bottom-end">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.tag === '单人病房' ? 'primary' : 'success'"
              disable-transitions>{{ scope.row.tag }}
          </el-tag>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "NurseRecord",
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
      searchForm: {
        name: '',
        region: ''
      },
      form: {
        name: '',
        region: '',
        date1: '',
        date2: '',
        delivery: false,
        type: [],
        resource: '',
        desc: ''
      }
    }
  },
  methods: {
    handleClick(row) {
      console.log(row);
    }
  }
}
</script>
