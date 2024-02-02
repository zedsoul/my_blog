package com.example.blogserver.Controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.Vo.deletSongsVo;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.FavoriteSong;
import com.example.blogserver.service.IFavoriteSongService;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zlc
 * @since 2024-02-02
 */
@RestController
@RequestMapping("/favoritesong")
@Api(value = "歌曲模块", description = "收藏歌曲模块的接口信息")
public class FavoriteSongController {
    @Autowired
    IFavoriteSongService favoriteSongService;

    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "查看歌曲信息")
    @GetMapping("/getsong")
    public R getSongs(){
        String id= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
       return  R.data(favoriteSongService.list(new LambdaQueryWrapper<FavoriteSong>().eq(FavoriteSong::getUserId,id)),"获取歌曲成功！");
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "删除歌曲信息")
    @PostMapping("/deletesong")
    public R deletesong(@RequestBody deletSongsVo deletSongsVo){
        String id= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        return  R.data(favoriteSongService.remove(new LambdaQueryWrapper<FavoriteSong>().eq(FavoriteSong::getUserId,id).eq(FavoriteSong::getSongId,deletSongsVo.getSongId())),"删除歌曲成功！");
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "更新用户信息")
    @PostMapping("/addsong")
    public R addsong(@RequestBody FavoriteSong favoriteSong) {
        String id = JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        favoriteSong.setUserId(Long.valueOf(id));
        LambdaQueryWrapper<FavoriteSong> eq = new LambdaQueryWrapper<FavoriteSong>().eq(FavoriteSong::getUserId, id).eq(FavoriteSong::getSongId, favoriteSong.getSongId());
        int count = favoriteSongService.count(eq);
        if (count == 0) {

            return R.data(favoriteSongService.save(favoriteSong), "添加歌曲成功！");
        } else {
            return R.fail(100, "歌曲重复收藏了哦！！");
        }
    }
}
