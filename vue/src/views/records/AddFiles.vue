<template>
  <div>
    <div class="manager-header-right">
      <div class="avatar">
        <img :src="'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"/>
        <div style="color: #666666">{{ '管理员' }}</div>
      </div>
    </div>
    <hr>
    <div style="margin-left: 20px">
      <div>就诊患者</div>
      <div style="margin: 10px auto">
        <el-row style="">
          <el-col :span="16">
            <div class="grid-content bg-purple">
              <el-form :inline="true" :model="formInline" class="demo-form-inline" size="small">
                <el-form-item label="患者姓名">
                  <el-input v-model="formInline.user" placeholder="患者姓名"></el-input>
                </el-form-item>
                <el-form-item label="性别">
                  <el-select v-model="formInline.gender" placeholder="性别">
                    <el-option label="男" value="男"></el-option>
                    <el-option label="女" value="女"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="年龄">
                  <el-input v-model="formInline.age" placeholder="年龄"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">详细信息</el-button>
                </el-form-item>
              </el-form>
              <el-row>
                <el-col :span="12">
                  <div class="grid-content bg-purple">
                    <div style="text-align:center">
                      诊断结果
                    </div>
                    <div>
                      <el-input
                          type="textarea"
                          :autosize="{ minRows: 15, maxRows: 17}"
                          placeholder="请输入内容"
                          v-model="textarea1">
                      </el-input>
                    </div>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="grid-content bg-purple-light">
                    <div style="text-align:center">
                      处理措施
                    </div>
                    <div style="margin:0 0 0 15px">
                      <el-row :gutter="15">
                        <el-col :span="20">
                          <el-input v-model="text1" placeholder="请输入内容" size="small"></el-input>
                        </el-col>
                        <el-col :span="4">
                          <el-button type="primary" @click="add" size="small">添加</el-button>
                        </el-col>
                      </el-row>

                    </div>
                    <div style="margin: 7px">
                      <el-input style="margin-left: 5px"
                                type="textarea"
                                :autosize="{ minRows: 13, maxRows: 17}"
                                placeholder="珍珠明目滴眼液，一瓶，-天3-4次;                                       聚乙烯醇滴眼液，一盒，一天1支;"
                                v-model="textarea2">
                      </el-input>
                    </div>
                  </div>
                </el-col>
              </el-row>
              <div style="margin-left: 420px">
                <el-button type="primary" @click="add" size="small">病例保存</el-button>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="grid-content bg-purple-light">
              <div style="text-align:center">
                溯源病例
                <div>
                  <el-col :span="6">
                    <el-button type="primary" @click="add" size="small">病例保存</el-button>
                  </el-col>
                  <el-col :span="12">
                    <el-input v-model="text1" placeholder="请输入内容" size="small"></el-input>
                  </el-col>
                  <el-col :span="6">
                    <el-button type="primary" @click="add" size="small">病例保存</el-button>
                  </el-col>
                </div>
                <div>
                  <el-table
                      :data="tableData"
                      style="width: 100%">
                    <el-table-column
                        prop="id"
                        label="序号">
                    </el-table-column>
                    <el-table-column
                        prop="hospital"
                        label="医院">
                    </el-table-column>
                    <el-table-column
                      prop="department"
                      label="科室">
                    </el-table-column>
                    <el-table-column
                        prop="time"
                        label="时间">
                    </el-table-column>
                    <el-table-column
                        label="操作">
                      <el-button type="primary" @click="add" size="small">详细</el-button>
                    </el-table-column>
                  </el-table>
                </div>
                <div>
                  <el-pagination
                      small
                      layout="prev, pager, next"
                      :total="50">
                  </el-pagination>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>

</template>

<script>
export default {
  data() {
    return {
      textarea1: "",
      textarea2: "",
      tableData:[{
        id:"1",
        hospital:"大和医院",
        department:"皮肤科",
        time:"2024-3-02"
      },
      {
        id:"2",
        hospital:"大和医院",
        department:"内科",
        time:"2024-01-03"
      },
      {
        id:"3",
        hospital:"大和医院",
        department:"儿科",
        time:"2024-01-03"
      },
        {
          id:"4",
          hospital:"大和医院",
          department:"内科",
          time:"2024-01-03"
        },
        {
          id:"5",
          hospital:"大和医院",
          department:"内科",
          time:"2024-01-03"
        },
        {
          id:"6",
          hospital:"大和医院",
          department:"内科",
          time:"2024-01-03"
        }
      ],
      formInline: {
        user: '',
        gender: '',
        age: '',
      }
    }
  },
  methods: {
    onSubmit() {
      console.log('submit!');
    },
    add() {
      console.log('添加');
    }
  }
}
</script>

<style scoped>
.manager-header-right {
  width: 150px;
  display: block;
  margin-left: auto;

  align-items: center;
  justify-content: flex-end;
}

.manager-header-right .avatar {
  display: flex;
  align-items: center;
  padding-right: 10px;
  color: #ccc;
  cursor: default
}

.manager-header-right .avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 5px
}


</style>