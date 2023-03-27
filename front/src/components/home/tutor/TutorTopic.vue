<template>
    <div>
        <el-card class="box-card">
            <div slot="header">
                <el-row>
                    <el-col :span="12">
                        <div class="header_div">
                            <span>我的毕设题目</span>
                            <el-button class="header_btn" @click="openNewTopic" plain type="primary">新增毕设课题</el-button>
                            <el-button class="header_btn" plain type="success" @click="showDetail">查看审核表详情</el-button>

                        </div>
                    </el-col>

                    <el-col :span="12">
                        <div class="show_stu_number">
                            <span>{{ myTopicList.length }}/10</span>
                        </div>
                    </el-col>
                </el-row>
            </div>
            <div class="item">
                <el-table height="720" empty-text="暂无数据" :data="myTopicList" scripe border style="width: 100%">
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column prop="topicName" label="课题名称" min-width="400px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicType" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicType" prop="topicType" label="课题类别" min-width="100px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicSource" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicSource" prop="topicSource" label="课题来源" min-width="100px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicPassed" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicPassed" prop="passed" label="审核结果" min-width="100px">
                    </el-table-column>
                    <el-table-column prop="checkBy" label="审核员名称" min-width="100px">
                    </el-table-column>
                    <el-table-column prop="suggestions" label="修改建议" min-width="200px" width="200px">
                    </el-table-column>
                    <el-table-column prop="submitTime" label="提交时间" min-width="200px">
                    </el-table-column>
                    <el-table-column prop="checkTime" label="审核时间" min-width="200px">
                    </el-table-column>

                </el-table>
            </div>
        </el-card>
        <el-dialog :visible.sync="detailVisable" width="1000px">
            <Detail ref="checkTableDetailRef"></Detail>
        </el-dialog>

        <el-dialog :visible.sync="newTopicVisable" width="1000px">
            <NewTopic></NewTopic>
        </el-dialog>
    </div>
</template>
<script>
import NewTopic from '@/components/home/tutor/NewTopic.vue';

export default {

    name: "TutorTopic",
    components: {
        NewTopic
    },
    methods: {
        filterMethodTopicType(value, row) {
            return row.topicType === value
        },
        filterMethodTopicSource(value, row) {
            return row.topicSource === value
        },
        filterMethodTopicPassed(value, row) {
            return row.passed === value
        }
    },
    data() {
        return {
            filterTopicPassed: [
                {
                    text: "不及格",
                    value: "不及格"
                },
                {
                    text: "基本及格",
                    value: "基本及格"
                },
                {
                    text: "及格",
                    value: "及格"
                },
            ],
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

            detailVisable: false,
            newTopicVisable: false,
            myTopicList: []

        }

    },
    methods: {
        showDetail() {
            this.detailVisable = true
        },
        openNewTopic() {
            this.newTopicVisable = true
        }
    },
    created() {
        //发送请求，根据导师用户id获取毕设课题列表
        this.myTopicList = []


        for (let index = 0; index < 10; index++) {
            this.myTopicList.push(
                {
                    topicName: "基于xxx理念的某某某某管理系统",
                    topicType: "设计",
                    topicSource: "自命题",
                    submitTime: "提交时间",
                    passed: "不及格",
                    checkTime: new Date().toLocaleString(),
                    checkBy: "审核员名称",
                    suggestions: "修改建议"
                }
            )
        }
    }
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

.header_btn {
    font-size: 18px;
    margin-left: 20px;
    width: 180px;
}

.header_div {
    width: 520px;
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