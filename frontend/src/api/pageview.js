import request from "@/utils/request"

export const recordPageView = (type) => {
    return request.post("/pageview/record", null, { params: { type } })
}
