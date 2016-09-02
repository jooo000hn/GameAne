//
//  HTConnection.h
//  ChangedSDK
//
//  Created by 王璟鑫 on 16/4/15.
//  Copyright © 2016年 王璟鑫. All rights reserved.
//

#import <Foundation/Foundation.h>
/**
 *  支付页协议
 */
@protocol paymentDelegate <NSObject>

/**
 *  协议方法,关闭支付模态
 */
-(void)dismissPaymentView;

@end

@interface HTConnection : NSObject

@property id<paymentDelegate>delegate;

+(void)showHTSDK;


/**
 *  推出网页支付页面
 *
 *  @param payURL 支付链接
 */
+(void)showPaymentView:(NSString*)payURL;


/**
 *  关闭网页支付页面
 */
+(void)dismissPaymenController;


/**
 *  单利
 */
+(instancetype)defaultConnertHelper;



/*
 *在- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:方法中调用此方法,并输入你们自己的appID
 */
+(void)inputappID:(NSString*)appID channel:(NSString*)channel;



/**
 *  登陆后回调信息
 */
//@property(nonatomic,copy)void(^backBlock)(NSDictionary*);
@property(nonatomic,copy)void(^backBlock)(NSDictionary*login,NSDictionary*FBToken);
/**
 *  返回绑定结果
 */
@property(nonatomic,copy)void(^bindBlock)(NSDictionary*);
/**
 *  facebook分享回调
 */
@property(nonatomic,copy)void(^sharFB)(NSDictionary*);
/**
 *  faceBook邀请回调
 */
@property(nonatomic,copy)void(^inviteFB)(NSDictionary*);
//注册成功回调
@property(nonatomic,copy)void(^registerBlock)(NSDictionary*);

/**
 *  统计接口
 *
 *  @param type       类型log或者reg
 *  @param version    游戏版本
 *  @param coo_server 合作伙伴服务器id
 *  @param coo_uid    合作伙伴用户UID
 */
+(void)StatisticsInterface:(NSString*)type version:(NSString*)version coo_server:(NSString*)coo_server coo_uid:(NSString*)coo_uid;

/**
 *  faceBook分享
 *
 *  @param url               链接
 *  @param imageURL          图片链接
 *  @param contentTitle      标题
 */
+ (void)shareToFacebookWithURL:(NSString*)url imageURL:(NSString*)imageURL contentTitle:(NSString*)contentTitle contentDescription:(NSString*)contentDescription;
/**
 *  faceBook邀请
 *
 *  @param title             标题
 *  @param msg               详细内容
 */
+ (void)inviteFB:(NSString *)title message:(NSString *)msg;


/**
 *  获取商品ID列表
 *
 *  @param server  现在默认是1后续版本会变
 *  @param success 请求成功回调
 *  @param error   请求失败回调
 */
+(void)getProductsIDwithServer:(NSString*)server ifSuccess:(void(^)(NSArray* response))success orError:(void(^)(NSError*error))error;


@end
