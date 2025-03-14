package com.shaohao.mytask.service;


import com.shaohao.mytask.utils.Web3Until;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * web3服务类
 * </p>
 *
 * @author shaohao
 * @since 2023-12-02
 */
public class Web3Service {

    /**
     * 获取代币精度
     *
     * @return BigInteger decimals
     */
    public Integer getTokenDecimals(Web3j web3j, String contractAddress){
        // 代币合约函数名和参数
        String functionName = "decimals";
        return Numeric.toBigInt(eth_Call(web3j,contractAddress,functionName)).intValue();
    }

    /**
     * 获取代币符号
     *
     * @return 代币符号
     */
    public String getTokenSymbol(Web3j web3j, String contractAddress){
        // 代币合约函数名和参数
        String functionName = "symbol";
        return Web3Until.hexToString(eth_Call(web3j,contractAddress,functionName));
    }

    /**
     * 获取代币余额
     *
     * @return BigInteger decimals
     */
    public BigDecimal getAccountBalance(Web3j web3j, String accountAddress, String tokenContractAddress){
        try {
            // 获取代币余额
            EthCall response = web3j.ethCall(
                    Transaction.createEthCallTransaction(accountAddress, tokenContractAddress,
                            "0x70a08231000000000000000000000000" + Numeric.cleanHexPrefix(accountAddress)),
                    DefaultBlockParameterName.LATEST)
                    .send();

            String result = response.getResult();
            Integer decimals = getTokenDecimals(web3j,tokenContractAddress);
            String symbol = getTokenSymbol(web3j,tokenContractAddress);
            BigDecimal balanceToken = BigDecimal.ZERO;
            if (!result.equals("0x")){
                BigDecimal balance = new BigDecimal(Numeric.toBigInt(result));
                // 根据代币的小数位数进行转换
                 balanceToken = balance.divide(BigDecimal.TEN.pow(decimals));
            }
            System.out.println("账户余额：" + balanceToken + " " + symbol);
            return balanceToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  立即执行新的消息调用，而不在区块链上创建交易
     *  代币合约函数名和函数名
     * @return BigInteger decimals
     */
    public String eth_Call(Web3j web3j, String contractAddress, String functionName){

        List<TypeReference<?>> outputParameters = Arrays.asList(new TypeReference<Uint>() {});
        // 构造代币合约函数对象
        Function function = new Function(functionName, Collections.emptyList(), outputParameters);
        // 构造代币合约调用交易
        String encodedFunction = FunctionEncoder.encode(function);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(null, contractAddress, encodedFunction);
        try {
            // 发送代币合约调用交易
            EthCall response = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
            return response.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取代币余额
     *
     * @return BigInteger decimals
     */
    public BigDecimal getAccountBalance2(Web3j web3j, String accountAddress, String tokenContractAddress){
        try {
            DefaultBlockParameter defaultBlockParameter = DefaultBlockParameterName.LATEST;
            // 获取代币合约的balanceOf方法的ABI编码
            Function balanceOfFunction = new Function(
                    "balanceOf",
                    Arrays.asList(new Address(accountAddress)),
                    Arrays.asList(new TypeReference<Uint256>() {})
            );
            String encodedFunction = FunctionEncoder.encode(balanceOfFunction);

            // 发送以太坊调用请求
            EthCall response = web3j.ethCall(
                    Transaction.createEthCallTransaction(accountAddress, tokenContractAddress, encodedFunction),
                    defaultBlockParameter
            ).send();

            // 解析响应数据
            String result = response.getValue();
            Integer decimals = getTokenDecimals(web3j,tokenContractAddress);
            String symbol = getTokenSymbol(web3j,tokenContractAddress);
            BigDecimal balanceToken = BigDecimal.ZERO;
            if (!result.equals("0x")){
                BigDecimal balance = new BigDecimal(Numeric.toBigInt(result));
                // 根据代币的小数位数进行转换
                balanceToken = balance.divide(BigDecimal.TEN.pow(decimals));
            }
            System.out.println("账户余额：" + balanceToken + " " + symbol);
            return balanceToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
