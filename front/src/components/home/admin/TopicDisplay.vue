<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="card_header">
                <span>课题信息</span>
            </div>
            <div class="item">
                <el-table height="712" empty-text="暂无数据" :data="topicList" scripe border style="width: 100%">
                    <el-table-column type="index">
                    </el-table-column>
                    <el-table-column prop="topicName" label="课题名称" min-width="350px">
                    </el-table-column>
                    <el-table-column prop="major" label="专业" min-width="200px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicType" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicType" prop="topicType" label="课题类别" min-width="110px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicSource" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicSource" prop="topicSource" label="课题来源" min-width="110px">
                    </el-table-column>
                    <el-table-column :filters="filterTopicPassed" :filter-multiple="false" filter-placement="bottom"
                        :filter-method="filterMethodTopicPassed" prop="passed" label="审核结果" min-width="110px">
                    </el-table-column>
                    <el-table-column prop="checkBy" label="审核员名称" min-width="120px">
                    </el-table-column>
                    <el-table-column prop="suggestions" label="修改建议" min-width="190px" width="190px">
                    </el-table-column>
                    <el-table-column prop="submitTime" label="提交时间" min-width="200px">
                    </el-table-column>
                    <el-table-column prop="checkTime" label="审核时间" min-width="200px">
                    </el-table-column>
                </el-table>

                <!-- 分页 -->
                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                    :current-page="currentPage" :page-sizes="pageSizeList" :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper" :total="total">
                </el-pagination>
            </div>
        </el-card>
    </div>
</template>
<script>
export default {
    name: "TopicDisplay",
    methods: {

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
            topicList: [],

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
        for (var i = 1; i <= 15; i++) {
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



    }
}
</script>

<style scoped lang="less">
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