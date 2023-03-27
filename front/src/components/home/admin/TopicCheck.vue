<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <span>尚未审核的课题</span>
            </div>
            <div class="item">
                <el-table height="712" empty-text="暂无数据" :data="topicList" scripe border style="width: 100%">
                    <el-table-column fixed type="index">
                    </el-table-column>
                    <el-table-column fixed prop="topicName" label="课题名称" min-width="350px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicType" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicType" prop="topicType" label="课题类别" min-width="120px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicSource" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicSource" prop="topicSource" label="课题来源" min-width="120px">
                    </el-table-column>
                    <el-table-column prop="checkBy" label="提交人名称" min-width="120px">
                    </el-table-column>
                    <el-table-column prop="major" label="专业名称" min-width="120px">
                    </el-table-column>
                    <el-table-column prop="submitTime" label="提交时间" min-width="200px" width="200px">
                    </el-table-column>
                    <el-table-column min-width="100px">
                        <template slot-scope="scope">
                            <el-button class="btn_check" plain type="success" @click="showCheckFormDialog(scope)">审核
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <!-- 分页 -->
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-sizes="pageSizeList" :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="total">
                </el-pagination>
            </div>
        </el-card>

        <el-dialog title="课题审核详情页" :visible.sync="checkFormDialogVisible" width="600px">
            <div>
                <el-form label-position="top" :model="checkForm">
                    <el-form-item label="课题名称">
                        <el-input disabled v-model="checkForm.topicName"></el-input>
                    </el-form-item>
                    <el-form-item label="是否及格">
                        <el-select v-model="checkForm.passed" placeholder="是否及格">
                            <el-option v-for="item in passedList" :label="item.description" :value="item.pass_id"
                                :key="item.pass_id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="修改建议">
                        <el-input type="textarea" v-model="checkForm.suggestions"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-row class="btn_checkForm_div">
                            <el-button plain type="success" @click="checkSubmit">确认审核</el-button>
                            <el-button plain type="warning" @click="nextTopic">下一个课题</el-button>
                            <el-button plain type="danger" @click="closeCheckForm">关闭页面</el-button>
                        </el-row>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>
    </div>
</template>
<script>

export default {
    name: "TopicCheck",
    methods: {
        //下一个课题按钮
        nextTopic() { },

        checkSubmit() {
            //提交课题审核
            console.log("提交课题审核");
        },
        //展示毕设课题审核页面
        showCheckFormDialog(scope) {
            let rowData = scope.row
            this.checkForm.topicName = rowData.topicName
            this.checkFormDialogVisible = true

        },

        //关闭审核页面
        closeCheckForm() {
            this.checkFormDialogVisible = false
        },
        // 分页的pageSize
        handleSizeChange(val) {
            this.pageSize = val
        },
        // 分页的currentPage
        handleCurrentChange(val) {
            this.currentPage = val
        },

        filterMethodTopicType(value, row) {
            return row.topicType === value
        },
        filterMethodTopicSource(value, row) {
            return row.topicSource === value
        },

    },
    data() {
        return {
            checkForm: {
                topicName: "",
                passed: '',
                suggestions: ""
            },
            passedList: [],
            topicList: [],
            checkFormDialogVisible: false,
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

            // 分页
            total: '',
            currentPage: '',
            pageSize: '',
            pageSizeList: []
        }

    },
    created() {

        //分页
        this.total = 100
        this.currentPage = 1
        this.pageSizeList = [5, 10, 15, 20]
        this.pageSize = this.pageSizeList[0]

        //发送请求获取topicList
        this.topicList = []

        //整点假数据
        for (var i = 1; i <= 10; i++) {
            this.topicList.push(
                {
                    topicName: "基于xxx理念的某某某某管理系统",
                    topicType: "设计",
                    topicSource: "自命题",
                    submitTime: "提交时间",
                    passed: "不及格",
                    checkTime: new Date().toLocaleString(),
                    checkBy: "审核员名称",
                    suggestions: "修改建议",
                    major: "专业名称"
                },
            )
        }

        //发送请求获取passedList
        this.passedList = []

        //整点假数据
        this.passedList.push(
            {
                pass_id: 0,
                description: '不及格'
            },
            {
                pass_id: 1,
                description: '基本及格'
            },
            {
                pass_id: 2,
                description: '及格'
            },
        )
    }
}
</script>

<style scoped lang="less">
.btn_checkForm_div {
    text-align: center;
}

/deep/.el-form-item {
    text-align: left;
    font-size: 18px;
    line-height: 23px;
}

/deep/.el-dialog__header {
    line-height: 23px;
}

/deep/.el-form-item__label {
    font-size: 18px;
}

.btn_check {
    width: 100px;
    font-size: 18px;
}

.box-card {
    height: 818px;
    line-height: 23px;
    background-color: rgba(255, 255, 255, 0.9);
}

.card_header {
    text-align: left;
    font-size: 20px;
}

.el-table {
    text-align: center;
    font-size: 18px;
}

/deep/.el-table__row {
    height: 66px;
}
</style>