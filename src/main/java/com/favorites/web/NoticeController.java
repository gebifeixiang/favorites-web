package com.favorites.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.favorites.domain.Comment;
import com.favorites.domain.CommentRepository;
import com.favorites.domain.Notice;
import com.favorites.domain.NoticeRepository;
import com.favorites.domain.result.ExceptionMsg;
import com.favorites.domain.result.Response;
import com.favorites.utils.DateUtils;

/**
*@ClassName: NoticeController
*@Description: 
*@author YY 
*@date 2016年8月31日  上午9:59:47
*@version 1.0
*/

@RestController
@RequestMapping("/notice")
public class NoticeController extends BaseController{
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	/**
	 * 更新消息为已读
	 * @return
	 */
	@RequestMapping(value = "/updateAtMeNoticeReaded", method = RequestMethod.POST)
	public Response updateAtMeNoticeReaded() {
		logger.info("updateAtMeNoticeReaded begin");
		try {
			noticeRepository.updateReadedByUserId("read", getUserId());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateUserName failed, ", e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}
	
	/**
	 * 回复
	 * @param comment
	 * @return
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public Response reply(Comment comment) {
		logger.info("reply begin");
		try {
			comment.setUserId(getUserId());
			comment.setCreateTime(DateUtils.getCurrentTime());
			Comment saveCommon = commentRepository.save(comment);
			Notice notice = new Notice();
			notice.setCollectId(comment.getCollectId().toString());
			notice.setUserId(comment.getReplyUserId());
			notice.setType("comment");
			notice.setReaded("unread");
			notice.setOperId(saveCommon.getId().toString());
			notice.setCreateTime(DateUtils.getCurrentTime());
			noticeRepository.save(notice);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("updateUserName failed, ", e);
			return result(ExceptionMsg.FAILED);
		}
		return result();
	}

}