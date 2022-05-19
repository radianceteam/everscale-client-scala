package com.radiance.jvm.temp.abi

import com.radiance.jvm.temp.Mod._
import org.scalatest.flatspec.AnyFlatSpec
import cats.implicits._
import com.radiance.jvm._
import com.radiance.jvm.abi._
import com.radiance.jvm.boc.{ParamsOfParse, ResultOfParse}
import com.radiance.jvm.crypto._
import io.circe.Json._
import com.radiance.TestUtils
import org.scalatest.matchers.should.Matchers._

class AbiModuleTest extends AnyFlatSpec with TestUtils {

  private val client = new TestClient();
  private val (events_abi, events_tvc) = client.pack(EVENTS, V2.some)
  private val keys = KeyPair(
    public = "4c7c408ff1ddebb8d6405ee979c716a14fdd6cc08124107a61d3c25597099499",
    secret = "cc8929d635719612a9478b9cd17675a39cfad52d8959e8a177389b8c0b9122a7"
  )
  private val address = "0:05beb555e942fa744fd96f45a9ea9d0a8248208ca12421947c06e59bc997d309"
  private val time: BigInt = BigInt(1599458364291L)
  private val expire: Long = 1599458404

  private val unsigned_message =
    "te6ccgEBAgEAeAABpYgAC31qq9KF9Oifst6LU9U6FQSQQRlCSEMo+A3LN5MvphIFMfECP8d3ruNZAXul5xxahT91swIEkEHph08JVlwmUmQAAAXRnJcuDX1XMZBW+LBKAQBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
  private val signed_message =
    "te6ccgEBAwEAvAABRYgAC31qq9KF9Oifst6LU9U6FQSQQRlCSEMo+A3LN5MvphIMAQHhrd/b+MJ5Za+AygBc5qS/dVIPnqxCsM9PvqfVxutK+lnQEKzQoRTLYO6+jfM8TF4841bdNjLQwIDWL4UVFdxIhdMfECP8d3ruNZAXul5xxahT91swIEkEHph08JVlwmUmQAAAXRnJcuDX1XMZBW+LBKACAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=="
  private val data_to_sign = "i4Hs3PB12QA9UBFbOIpkG3JerHHqjm4LgvF4MA7TDsY="

  private val signing_box: RegisteredSigningBox = client.request[KeyPair, RegisteredSigningBox](
    "crypto.get_signing_box",
    keys
  )

  def deploy_params(signing: SignerADT.Signer): ParamsOfEncodeMessage = ParamsOfEncodeMessage(
    events_abi,
    None,
    DeploySet(events_tvc).some,
    CallSet(
      "constructor",
      FunctionHeader(
        expire.some,
        time.some,
        keys.public.some
      ).some,
      None
    ).some,
    signing,
    None
  )

  def run_params(signing: SignerADT.Signer): ParamsOfEncodeMessage = ParamsOfEncodeMessage(
    events_abi,
    address.some,
    None,
    CallSet(
      "returnValue",
      FunctionHeader(
        pubkey = None,
        time = time.some,
        expire = expire.some
      ).some,
      fromFields(Seq("id" -> fromString("0"))).some
    ).some,
    signing,
    None
  )

  def body_params(run_params: ParamsOfEncodeMessage): ParamsOfEncodeMessageBody = ParamsOfEncodeMessageBody(
    run_params.abi,
    run_params.call_set.get,
    false,
    run_params.signer,
    run_params.processing_try_index
  )

  def extract_body(message: String): Value = {
    val unsigned_parsed: ResultOfParse = client.request[ParamsOfParse, ResultOfParse](
      "boc.parse_message",
      ParamsOfParse(message)
    )
    unsigned_parsed.parsed.hcursor.get[Value]("body").get
  }

  // TODO
  def abi_uint(value: BigInt, size: Int): String = {
    val str = value.toString(16)

    val length = size / 4 - str.length + 1
    s"""0x${String.format("%1$" + length + "s", str).replace(' ', '0')}"""
  }

  behavior of "encode_v2"

  it should "perform first test" in {

    val unsigned: ResultOfEncodeMessage = client.request[ParamsOfEncodeMessage, ResultOfEncodeMessage](
      "abi.encode_message",
      deploy_params(SignerADT.External(keys.public))
    )

    unsigned.message shouldBe "te6ccgECFwEAA2gAAqeIAAt9aqvShfTon7Lei1PVOhUEkEEZQkhDKPgNyzeTL6YSEZTHxAj/Hd67jWQF7peccWoU/dbMCBJBB6YdPCVZcJlJkAAAF0ZyXLg19VzGRotV8/gGAQEBwAICA88gBQMBAd4EAAPQIABB2mPiBH+O713GsgL3S844tQp+62YECSCD0w6eEqy4TKTMAib/APSkICLAAZL0oOGK7VNYMPShCQcBCvSkIPShCAAAAgEgDAoByP9/Ie1E0CDXScIBjhDT/9M/0wDRf/hh+Gb4Y/hijhj0BXABgED0DvK91wv/+GJw+GNw+GZ/+GHi0wABjh2BAgDXGCD5AQHTAAGU0/8DAZMC+ELiIPhl+RDyqJXTAAHyeuLTPwELAGqOHvhDIbkgnzAg+COBA+iogggbd0Cgud6S+GPggDTyNNjTHwH4I7zyudMfAfAB+EdukvI83gIBIBINAgEgDw4AvbqLVfP/hBbo417UTQINdJwgGOENP/0z/TANF/+GH4Zvhj+GKOGPQFcAGAQPQO8r3XC//4YnD4Y3D4Zn/4YeLe+Ebyc3H4ZtH4APhCyMv/+EPPCz/4Rs8LAMntVH/4Z4AgEgERAA5biABrW/CC3Rwn2omhp/+mf6YBov/ww/DN8Mfwxb30gyupo6H0gb+j8IpA3SRg4b3whXXlwMnwAZGT9ghBkZ8KEZ0aCBAfQAAAAAAAAAAAAAAAAACBni2TAgEB9gBh8IWRl//wh54Wf/CNnhYBk9qo//DPAAxbmTwqLfCC3Rwn2omhp/+mf6YBov/ww/DN8Mfwxb2uG/8rqaOhp/+/o/ABkRe4AAAAAAAAAAAAAAAAIZ4tnwOfI48sYvRDnhf/kuP2AGHwhZGX//CHnhZ/8I2eFgGT2qj/8M8AIBSBYTAQm4t8WCUBQB/PhBbo4T7UTQ0//TP9MA0X/4Yfhm+GP4Yt7XDf+V1NHQ0//f0fgAyIvcAAAAAAAAAAAAAAAAEM8Wz4HPkceWMXohzwv/yXH7AMiL3AAAAAAAAAAAAAAAABDPFs+Bz5JW+LBKIc8L/8lx+wAw+ELIy//4Q88LP/hGzwsAye1UfxUABPhnAHLccCLQ1gIx0gAw3CHHAJLyO+Ah1w0fkvI84VMRkvI74cEEIoIQ/////byxkvI84AHwAfhHbpLyPN4="
    unsigned.data_to_sign shouldBe "KCGM36iTYuCYynk+Jnemis+mcwi3RFCke95i7l96s4Q=".some

    val signature = client.sign_detached(unsigned.data_to_sign.get, keys);
    signature shouldBe "6272357bccb601db2b821cb0f5f564ab519212d242cf31961fe9a3c50a30b236012618296b4f769355c0e9567cd25b366f3c037435c498c82e5305622adbc70e"

    val signed: ResultOfAttachSignature = client
      .request[ParamsOfAttachSignature, ResultOfAttachSignature](
        "abi.attach_signature",
        ParamsOfAttachSignature(
          events_abi,
          keys.public,
          unsigned.message,
          signature
        )
      )
    signed.message shouldBe "te6ccgECGAEAA6wAA0eIAAt9aqvShfTon7Lei1PVOhUEkEEZQkhDKPgNyzeTL6YSEbAHAgEA4bE5Gr3mWwDtlcEOWHr6slWoyQlpIWeYyw/00eKFGFkbAJMMFLWnu0mq4HSrPmktmzeeAboa4kxkFymCsRVt44dTHxAj/Hd67jWQF7peccWoU/dbMCBJBB6YdPCVZcJlJkAAAF0ZyXLg19VzGRotV8/gAQHAAwIDzyAGBAEB3gUAA9AgAEHaY+IEf47vXcayAvdLzji1Cn7rZgQJIIPTDp4SrLhMpMwCJv8A9KQgIsABkvSg4YrtU1gw9KEKCAEK9KQg9KEJAAACASANCwHI/38h7UTQINdJwgGOENP/0z/TANF/+GH4Zvhj+GKOGPQFcAGAQPQO8r3XC//4YnD4Y3D4Zn/4YeLTAAGOHYECANcYIPkBAdMAAZTT/wMBkwL4QuIg+GX5EPKoldMAAfJ64tM/AQwAao4e+EMhuSCfMCD4I4ED6KiCCBt3QKC53pL4Y+CANPI02NMfAfgjvPK50x8B8AH4R26S8jzeAgEgEw4CASAQDwC9uotV8/+EFujjXtRNAg10nCAY4Q0//TP9MA0X/4Yfhm+GP4Yo4Y9AVwAYBA9A7yvdcL//hicPhjcPhmf/hh4t74RvJzcfhm0fgA+ELIy//4Q88LP/hGzwsAye1Uf/hngCASASEQDluIAGtb8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFvfSDK6mjofSBv6PwikDdJGDhvfCFdeXAyfABkZP2CEGRnwoRnRoIEB9AAAAAAAAAAAAAAAAAAIGeLZMCAQH2AGHwhZGX//CHnhZ/8I2eFgGT2qj/8M8ADFuZPCot8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFva4b/yupo6Gn/7+j8AGRF7gAAAAAAAAAAAAAAAAhni2fA58jjyxi9EOeF/+S4/YAYfCFkZf/8IeeFn/wjZ4WAZPaqP/wzwAgFIFxQBCbi3xYJQFQH8+EFujhPtRNDT/9M/0wDRf/hh+Gb4Y/hi3tcN/5XU0dDT/9/R+ADIi9wAAAAAAAAAAAAAAAAQzxbPgc+Rx5YxeiHPC//JcfsAyIvcAAAAAAAAAAAAAAAAEM8Wz4HPklb4sEohzwv/yXH7ADD4QsjL//hDzws/+EbPCwDJ7VR/FgAE+GcActxwItDWAjHSADDcIccAkvI74CHXDR+S8jzhUxGS8jvhwQQighD////9vLGS8jzgAfAB+EdukvI83g=="
  }

  it should "perform second test" in {

    val signed: ResultOfEncodeMessage = client.request[ParamsOfEncodeMessage, ResultOfEncodeMessage](
      "abi.encode_message",
      deploy_params(SignerADT.Keys(keys))
    )
    signed.message shouldBe "te6ccgECGAEAA6wAA0eIAAt9aqvShfTon7Lei1PVOhUEkEEZQkhDKPgNyzeTL6YSEbAHAgEA4bE5Gr3mWwDtlcEOWHr6slWoyQlpIWeYyw/00eKFGFkbAJMMFLWnu0mq4HSrPmktmzeeAboa4kxkFymCsRVt44dTHxAj/Hd67jWQF7peccWoU/dbMCBJBB6YdPCVZcJlJkAAAF0ZyXLg19VzGRotV8/gAQHAAwIDzyAGBAEB3gUAA9AgAEHaY+IEf47vXcayAvdLzji1Cn7rZgQJIIPTDp4SrLhMpMwCJv8A9KQgIsABkvSg4YrtU1gw9KEKCAEK9KQg9KEJAAACASANCwHI/38h7UTQINdJwgGOENP/0z/TANF/+GH4Zvhj+GKOGPQFcAGAQPQO8r3XC//4YnD4Y3D4Zn/4YeLTAAGOHYECANcYIPkBAdMAAZTT/wMBkwL4QuIg+GX5EPKoldMAAfJ64tM/AQwAao4e+EMhuSCfMCD4I4ED6KiCCBt3QKC53pL4Y+CANPI02NMfAfgjvPK50x8B8AH4R26S8jzeAgEgEw4CASAQDwC9uotV8/+EFujjXtRNAg10nCAY4Q0//TP9MA0X/4Yfhm+GP4Yo4Y9AVwAYBA9A7yvdcL//hicPhjcPhmf/hh4t74RvJzcfhm0fgA+ELIy//4Q88LP/hGzwsAye1Uf/hngCASASEQDluIAGtb8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFvfSDK6mjofSBv6PwikDdJGDhvfCFdeXAyfABkZP2CEGRnwoRnRoIEB9AAAAAAAAAAAAAAAAAAIGeLZMCAQH2AGHwhZGX//CHnhZ/8I2eFgGT2qj/8M8ADFuZPCot8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFva4b/yupo6Gn/7+j8AGRF7gAAAAAAAAAAAAAAAAhni2fA58jjyxi9EOeF/+S4/YAYfCFkZf/8IeeFn/wjZ4WAZPaqP/wzwAgFIFxQBCbi3xYJQFQH8+EFujhPtRNDT/9M/0wDRf/hh+Gb4Y/hi3tcN/5XU0dDT/9/R+ADIi9wAAAAAAAAAAAAAAAAQzxbPgc+Rx5YxeiHPC//JcfsAyIvcAAAAAAAAAAAAAAAAEM8Wz4HPklb4sEohzwv/yXH7ADD4QsjL//hDzws/+EbPCwDJ7VR/FgAE+GcActxwItDWAjHSADDcIccAkvI74CHXDR+S8jzhUxGS8jvhwQQighD////9vLGS8jzgAfAB+EdukvI83g=="

    val signed_with_box: ResultOfEncodeMessage = client.request[ParamsOfEncodeMessage, ResultOfEncodeMessage](
      "abi.encode_message",
      deploy_params(SignerADT.SigningBox(signing_box.handle))
    )
    signed_with_box.message shouldBe "te6ccgECGAEAA6wAA0eIAAt9aqvShfTon7Lei1PVOhUEkEEZQkhDKPgNyzeTL6YSEbAHAgEA4bE5Gr3mWwDtlcEOWHr6slWoyQlpIWeYyw/00eKFGFkbAJMMFLWnu0mq4HSrPmktmzeeAboa4kxkFymCsRVt44dTHxAj/Hd67jWQF7peccWoU/dbMCBJBB6YdPCVZcJlJkAAAF0ZyXLg19VzGRotV8/gAQHAAwIDzyAGBAEB3gUAA9AgAEHaY+IEf47vXcayAvdLzji1Cn7rZgQJIIPTDp4SrLhMpMwCJv8A9KQgIsABkvSg4YrtU1gw9KEKCAEK9KQg9KEJAAACASANCwHI/38h7UTQINdJwgGOENP/0z/TANF/+GH4Zvhj+GKOGPQFcAGAQPQO8r3XC//4YnD4Y3D4Zn/4YeLTAAGOHYECANcYIPkBAdMAAZTT/wMBkwL4QuIg+GX5EPKoldMAAfJ64tM/AQwAao4e+EMhuSCfMCD4I4ED6KiCCBt3QKC53pL4Y+CANPI02NMfAfgjvPK50x8B8AH4R26S8jzeAgEgEw4CASAQDwC9uotV8/+EFujjXtRNAg10nCAY4Q0//TP9MA0X/4Yfhm+GP4Yo4Y9AVwAYBA9A7yvdcL//hicPhjcPhmf/hh4t74RvJzcfhm0fgA+ELIy//4Q88LP/hGzwsAye1Uf/hngCASASEQDluIAGtb8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFvfSDK6mjofSBv6PwikDdJGDhvfCFdeXAyfABkZP2CEGRnwoRnRoIEB9AAAAAAAAAAAAAAAAAAIGeLZMCAQH2AGHwhZGX//CHnhZ/8I2eFgGT2qj/8M8ADFuZPCot8ILdHCfaiaGn/6Z/pgGi//DD8M3wx/DFva4b/yupo6Gn/7+j8AGRF7gAAAAAAAAAAAAAAAAhni2fA58jjyxi9EOeF/+S4/YAYfCFkZf/8IeeFn/wjZ4WAZPaqP/wzwAgFIFxQBCbi3xYJQFQH8+EFujhPtRNDT/9M/0wDRf/hh+Gb4Y/hi3tcN/5XU0dDT/9/R+ADIi9wAAAAAAAAAAAAAAAAQzxbPgc+Rx5YxeiHPC//JcfsAyIvcAAAAAAAAAAAAAAAAEM8Wz4HPklb4sEohzwv/yXH7ADD4QsjL//hDzws/+EbPCwDJ7VR/FgAE+GcActxwItDWAjHSADDcIccAkvI74CHXDR+S8jzhUxGS8jvhwQQighD////9vLGS8jzgAfAB+EdukvI83g=="

  }

  it should "perform third test" in {

    val unsigned: ResultOfEncodeMessage = client
      .request[ParamsOfEncodeMessage, ResultOfEncodeMessage](
        "abi.encode_message",
        run_params(SignerADT.External(keys.public))
      )

    unsigned.message shouldBe unsigned_message
    unsigned.data_to_sign shouldBe data_to_sign.some

    val unsigned_body = extract_body(unsigned.message).as[String].get

    val unsigned_body_encoded: ResultOfEncodeMessageBody = client
      .request[ParamsOfEncodeMessageBody, ResultOfEncodeMessageBody](
        "abi.encode_message_body",
        body_params(run_params(SignerADT.External(keys.public)))
      )
    unsigned_body_encoded.body shouldBe unsigned_body
    unsigned_body_encoded.data_to_sign shouldBe unsigned.data_to_sign

    val signature = client.sign_detached(unsigned.data_to_sign.get, keys)
    signature shouldBe "5bbfb7f184f2cb5f019400b9cd497eeaa41f3d5885619e9f7d4fab8dd695f4b3a02159a1422996c1dd7d1be67898bc79c6adba6c65a18101ac5f0a2a2bb8910b"

    val signed: ResultOfAttachSignature = client
      .request[ParamsOfAttachSignature, ResultOfAttachSignature](
        "abi.attach_signature",
        ParamsOfAttachSignature(
          events_abi,
          keys.public,
          unsigned.message,
          signature
        )
      )
    signed.message shouldBe signed_message

    val signed1: ResultOfAttachSignatureToMessageBody = client
      .request[ParamsOfAttachSignatureToMessageBody, ResultOfAttachSignatureToMessageBody](
        "abi.attach_signature_to_message_body",
        ParamsOfAttachSignatureToMessageBody(
          events_abi,
          keys.public,
          unsigned_body_encoded.body,
          signature
        )
      )
    val signed_body = extract_body(signed_message).as[String].get
    signed1.body shouldBe signed_body
  }

  it should "perform fourth test" in {

    val signed: ResultOfEncodeMessage = client
      .request[ParamsOfEncodeMessage, ResultOfEncodeMessage](
        "abi.encode_message",
        run_params(SignerADT.Keys(keys))
      )

    signed.message shouldBe signed_message

    val signed1: ResultOfEncodeMessageBody = client
      .request[ParamsOfEncodeMessageBody, ResultOfEncodeMessageBody](
        "abi.encode_message_body",
        body_params(run_params(SignerADT.Keys(keys)))
      )
    val signed_body = extract_body(signed_message).as[String].get
    signed1.body shouldBe signed_body

    val signed2: ResultOfEncodeMessage = client
      .request[ParamsOfEncodeMessage, ResultOfEncodeMessage](
        "abi.encode_message",
        run_params(SignerADT.SigningBox(signing_box.handle))
      )
    signed2.message shouldBe signed_message

    val signed3: ResultOfEncodeMessageBody = client
      .request[ParamsOfEncodeMessageBody, ResultOfEncodeMessageBody](
        "abi.encode_message_body",
        body_params(run_params(SignerADT.SigningBox(signing_box.handle)))
      )
    signed3.body shouldBe signed_body
  }

  it should "perform fifth test" in {

    val no_pubkey: ResultOfEncodeMessage = client
      .request[ParamsOfEncodeMessage, ResultOfEncodeMessage]("abi.encode_message", run_params(SignerADT.None))
    no_pubkey.message shouldBe "te6ccgEBAQEAVQAApYgAC31qq9KF9Oifst6LU9U6FQSQQRlCSEMo+A3LN5MvphIAAAAC6M5Llwa+q5jIK3xYJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAB"

    val no_pubkey_body: ResultOfEncodeMessageBody = client
      .request[ParamsOfEncodeMessageBody, ResultOfEncodeMessageBody](
        "abi.encode_message_body",
        body_params(run_params(SignerADT.None))
      )
    no_pubkey_body.body shouldBe extract_body(no_pubkey.message).as[String].get
  }

  behavior of "decode_v2"

  it should " " in {
    def decode_events(message: String) = {
      val result: DecodedMessageBody = client
        .request[ParamsOfDecodeMessage, DecodedMessageBody](
          "abi.decode_message",
          ParamsOfDecodeMessage(
            events_abi,
            message,
            None
          )
        )

      val parsed: ResultOfParse = client.request[ParamsOfParse, ResultOfParse](
        "boc.parse_message",
        ParamsOfParse(message)
      )
      val body = parsed.parsed.hcursor.get[String]("body").get
      val result_body: DecodedMessageBody = client
        .request[ParamsOfDecodeMessageBody, DecodedMessageBody](
          "abi.decode_message_body",
          ParamsOfDecodeMessageBody(
            events_abi,
            body,
            is_internal = parsed.parsed.hcursor.get[String]("msg_type_name").get == "Internal",
            None
          )
        )
      assert(result == result_body)
      result
    }

    val expected = DecodedMessageBody(
      body_type = MessageBodyTypeEnum.Input,
      name = "returnValue",
      value = fromFields(Seq("id" -> fromString(abi_uint(0, 256)))).some,
      header = FunctionHeader(
        expire = 1599458404L.some,
        time = BigInt(1599458364291L).some,
        pubkey = "4c7c408ff1ddebb8d6405ee979c716a14fdd6cc08124107a61d3c25597099499".some
      ).some
    )
    assert(
      expected == decode_events(
        "te6ccgEBAwEAvAABRYgAC31qq9KF9Oifst6LU9U6FQSQQRlCSEMo+A3LN5MvphIMAQHhrd/b+MJ5Za+AygBc5qS/dVIPnqxCsM9PvqfVxutK+lnQEKzQoRTLYO6+jfM8TF4841bdNjLQwIDWL4UVFdxIhdMfECP8d3ruNZAXul5xxahT91swIEkEHph08JVlwmUmQAAAXRnJcuDX1XMZBW+LBKACAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=="
      )
    )

    val expected1 = DecodedMessageBody(
      body_type = MessageBodyTypeEnum.Event,
      name = "EventThrown",
      value = fromFields(Seq("id" -> fromString(abi_uint(0, 256)))).some,
      header = None
    )
    assert(
      expected1 == decode_events(
        "te6ccgEBAQEAVQAApeACvg5/pmQpY4m61HmJ0ne+zjHJu3MNG8rJxUDLbHKBu/AAAAAAAAAMJL6z6ro48sYvAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABA"
      )
    )

    val result: DecodedMessageBody = client.request[ParamsOfDecodeMessageBody, DecodedMessageBody](
      "abi.decode_message_body",
      ParamsOfDecodeMessageBody(
        abi = events_abi,
        body =
          "te6ccgEBAgEAlgAB4a3f2/jCeWWvgMoAXOakv3VSD56sQrDPT76n1cbrSvpZ0BCs0KEUy2Duvo3zPExePONW3TYy0MCA1i+FFRXcSIXTHxAj/Hd67jWQF7peccWoU/dbMCBJBB6YdPCVZcJlJkAAAF0ZyXLg19VzGQVviwSgAQBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
        is_internal = false,
        None
      )
    )

    val expected2 = DecodedMessageBody(
      body_type = MessageBodyTypeEnum.Input,
      name = "returnValue",
      value = fromFields(Seq("id" -> fromString(abi_uint(0, 256)))).some,
      header = FunctionHeader(
        expire = 1599458404L.some,
        time = BigInt(1599458364291L).some,
        pubkey = "4c7c408ff1ddebb8d6405ee979c716a14fdd6cc08124107a61d3c25597099499".some
      ).some
    )
    assert(expected2 == result)

    val expected3 = DecodedMessageBody(
      body_type = MessageBodyTypeEnum.Output,
      name = "returnValue",
      value = fromFields(Seq("value0" -> fromString(abi_uint(0, 256)))).some,
      header = None
    )
    assert(
      expected3 == decode_events(
        "te6ccgEBAQEAVQAApeACvg5/pmQpY4m61HmJ0ne+zjHJu3MNG8rJxUDLbHKBu/AAAAAAAAAMKr6z6rxK3xYJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABA"
      )
    )
  }
}
