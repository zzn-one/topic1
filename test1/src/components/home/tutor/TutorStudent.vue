<template>
    <div class="detail_main">
        <el-card class="box-card">
            <div slot="header">
                <el-row>
                    <el-col :span="12">
                        <div>
                            <span>我指导的毕业生</span>
                        </div>
                    </el-col>
                    <el-col :span="12">
                        <div class="show_stu_number">
                            <span>{{ myStudentList.length }}/10</span>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <div class="item">
                <el-table empty-text="暂无数据" height="720" :data="myStudentList" scripe border style="width: 100%">
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column prop="topicName" label="课题名称" min-width="400px">
                    </el-table-column>
                    <el-table-column prop="sname" label="学生姓名" min-width="100px">
                    </el-table-column>
                    <el-table-column prop="sid" label="学号" min-width="150px">
                    </el-table-column>
                    <el-table-column prop="phone" label="学生手机号" min-width="150px">
                    </el-table-column>
                    <el-table-column prop="email" label="学生邮箱" min-width="200px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicType" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicType" prop="td" label="课题类别" min-width="100px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicSource" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicSource" prop="sd" label="课题来源" min-width="100px">
                    </el-table-column>
                </el-table>
            </div>
        </el-card>
    </div>
</template>
<script>
import jwt_prase from '@/assets/js/jwtPrase'

export default {
    name: "TutorStudent",
    methods: {
        filterMethodTopicType(value, row) {
            return row.td === value
        },
        filterMethodTopicSource(value, row) {
            return row.sd === value
        },
        async getStudentList() {
            let tid = jwt_prase().id
            const resp = await this.$http.get("/userTutor/getStudentList/" + tid)

            if (resp.data.code === 200 && resp.data.data !== null) {
                console.log(resp.data);
                this.myStudentList = resp.data.data
            }
        }
    },
    data() {
        return {
            filterTopicSource: [
                {
                    text: "自命题",
                    value: "自命题"
                },
                {
                    text: "企业",
                    value: "企业"
                },
                {
                    text: "教师科研",
                    value: "教师科研"
                },
            ],
            filterTopicType: [
                {
                    text: "论文",
                    value: "论文"
                },
                {
                    text: "设计",
                    value: "设计"
                },
            ],
            myStudentList: []
        }
    },
    created() {
        //发送请求，获取导师指导的学生
        this.getStudentList()

    },
}
</script>

<style scoped lang="less">
.box-card {
    line-height: 24px;
    min-height: 818px;
    text-align: left;
    font-size: 20px;
    background-color: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
}

.el-table {
    text-align: center;
    font-size: 18px;
}

/deep/.el-table__row {
    height: 66px;
}

.show_stu_number {
    float: right;
}
</style>