<template>
    <div>
        <el-table max-height="540px" :data="topicList" stripe style="width: 100%">
            <el-table-column type="index" >
            </el-table-column>
            <el-table-column prop="topicName" label="课题名称" min-width="400">
            </el-table-column>
            <el-table-column prop="topicType" label="课题类型" min-width="180">
            </el-table-column>
            <el-table-column prop="topicSource" label="课题来源">
            </el-table-column>
        </el-table>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
            :page-sizes="pageSizeList" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
    </div>

</template>

<script>
export default {
    name: "TopicPreview",
    data() {
        return {
            topicList: [],
            //分页
            total: '',
            currentPage: '',
            pageSize: '',
            pageSizeList: []
        }
    },
    methods: {
        handleSizeChange(val) {
            this.pageSize = val
        },
        handleCurrentChange(val) {
            this.currentPage = val
        }
    },
    created() {
        //分页数据
        this.total = 100,
            this.currentPage = 1,
            this.pageSizeList = [5, 10, 20, 30]
        this.pageSize = this.pageSizeList[0]

        //根据 毕业生用户的专业  获取 毕设课题列表
        this.topicList = []

        for (let index = 0; index < 100; index++) {
            this.topicList.push(
                {
                    topicName: '基于xxx的毕业设计管理系统',
                    topicType: '设计',
                    topicSource: '自命题'
                }
            )
        }

    }
}
</script>

<style scoped lang="less">
/deep/.el-table {
    font-size: 18px;
    color: black;
}
</style>