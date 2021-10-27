package edu.uic.cs474.f21.a4;

import java.util.HashMap;

public class Program {

    // 474
    public static Expression p1 = new Expression.IntConstant(474);

    // 400 + 74
    public static Expression p2 = new Expression.BinOpExpression(
            Expression.BinOpExpression.Operator.PLUS,
            new Expression.IntConstant(400),
            new Expression.IntConstant(74)
            );

    // 400 + (70 + 4)
    public static Expression p3 = new Expression.BinOpExpression(
            Expression.BinOpExpression.Operator.PLUS,
            new Expression.IntConstant(400),
            new Expression.BinOpExpression(
                    Expression.BinOpExpression.Operator.PLUS,
                    new Expression.IntConstant(70),
                    new Expression.IntConstant(4)
            )
    );

    // (let var = (400 + 70) in var + 4)
    public static Expression p4 = new Expression.LetExpression(
            new Name("var"),
            new Expression.BinOpExpression(
                    Expression.BinOpExpression.Operator.PLUS,
                    new Expression.IntConstant(400),
                    new Expression.IntConstant(70)
            ),
            new Expression.BinOpExpression(
                    Expression.BinOpExpression.Operator.PLUS,
                    new Expression.VariableExpression(new Name("var")),
                    new Expression.IntConstant(4)
            )
    );

    // (let v1 = 400 in
    //    (let v2 = 70 in v2)
    //    +
    //    4 + v1
    // )
    public static Expression p5 = new Expression.LetExpression(
            new Name("v1"),
            new Expression.IntConstant(400),
            new Expression.BinOpExpression(
                    Expression.BinOpExpression.Operator.PLUS,
                    new Expression.LetExpression(
                           new Name("v2"),
                           new Expression.IntConstant(70),
                           new Expression.VariableExpression(new Name("v2"))
                    ),
                    new Expression.BinOpExpression(
                            Expression.BinOpExpression.Operator.PLUS,
                            new Expression.IntConstant(4),
                            new Expression.VariableExpression(new Name("v1"))
                    )
            )
    );

    // (let var = 400 in
    //    (let var = 70 in var)
    //    +
    //    4 + var
    // )
    public static Expression p6 = new Expression.LetExpression(
            new Name("var"),
            new Expression.IntConstant(400),
            new Expression.BinOpExpression(
                    Expression.BinOpExpression.Operator.PLUS,
                    new Expression.LetExpression(
                            new Name("var"),
                            new Expression.IntConstant(70),
                            new Expression.VariableExpression(new Name("var"))
                    ),
                    new Expression.BinOpExpression(
                            Expression.BinOpExpression.Operator.PLUS,
                            new Expression.IntConstant(4),
                            new Expression.VariableExpression(new Name("var"))
                    )
            )
    );

    // (let v1 = 400 in
    //    (let v2 = 70 in v1+v2)
    //    +
    //    4
    // )
    public static Expression p7 = new Expression.LetExpression(
            new Name("v1"),
            new Expression.IntConstant(400),
            new Expression.BinOpExpression(
                    Expression.BinOpExpression.Operator.PLUS,
                    new Expression.LetExpression(
                            new Name("v2"),
                            new Expression.IntConstant(70),
                            new Expression.BinOpExpression(
                                    Expression.BinOpExpression.Operator.PLUS,
                                    new Expression.VariableExpression(new Name("v1")),
                                    new Expression.VariableExpression(new Name("v2"))
                            )
                    ),
                    new Expression.IntConstant(4)
            )
    );

    // (let divisor = 1 in (divisor == 0))
    public static Expression p8 = new Expression.LetExpression(
            new Name("divisor"),
            new Expression.IntConstant(1),
            new Expression.EqExpression(
                    new Expression.VariableExpression(new Name("divisor")),
                    new Expression.IntConstant(0)
            )
    );


    // (let dividend = 474 in
    //   (let divisor = 2 in
    //     (divisor == 0) ? 0 : dividend / divisor))
    public static Expression p9 = new Expression.LetExpression(
            new Name("dividend"),
            new Expression.IntConstant(474),
            new Expression.LetExpression(
                    new Name("divisor"),
                    new Expression.IntConstant(2),
                    new Expression.IfExpression(
                            new Expression.EqExpression(
                                    new Expression.VariableExpression(new Name("divisor")),
                                    new Expression.IntConstant(0)
                            ),
                            new Expression.IntConstant(0),
                            new Expression.BinOpExpression(
                                    Expression.BinOpExpression.Operator.DIV,
                                    new Expression.VariableExpression(new Name("dividend")),
                                    new Expression.VariableExpression(new Name("divisor"))
                            )
                    )
            )
    );

    // function safeDivision(dividend, divisor) -> (divisor == 0) ? 0 : dividend / divisor))
    //    safeDivision(400 + 74,0) + safeDivision(474,1)
    public static Expression p10 = new Expression.FunctionDeclExpression(
            new Name("safeDivision"),
            new Name[]{ new Name("dividend"), new Name("divisor")},
            new Expression.IfExpression(
                    new Expression.EqExpression(
                            new Expression.VariableExpression(new Name("divisor")),
                            new Expression.IntConstant(0)
                    ),
                    new Expression.IntConstant(0),
                    new Expression.BinOpExpression(
                            Expression.BinOpExpression.Operator.DIV,
                            new Expression.VariableExpression(new Name("dividend")),
                            new Expression.VariableExpression(new Name("divisor"))
                    )
            ),
            new Expression.BinOpExpression(
                    Expression.BinOpExpression.Operator.PLUS,
                    new Expression.FunctionCallExpression(
                            new Name("safeDivision"),
                            new Expression[]{
                                    new Expression.BinOpExpression(
                                            Expression.BinOpExpression.Operator.PLUS,
                                            new Expression.IntConstant(400),
                                            new Expression.IntConstant(74)
                                    ),
                                    new Expression.IntConstant(0)
                            }
                    ),
                    new Expression.FunctionCallExpression(
                            new Name("safeDivision"),
                            new Expression[]{
                                    new Expression.IntConstant(474),
                                    new Expression.IntConstant(1)
                            }
                    )
            )
    );

    // let error = -1 in
    //   function safeDivision(dividend, divisor) -> (divisor == 0) ? error : dividend / divisor))
    //      let error = -474 in
    //        safeDivision(400 + 74,0) + safeDivision(474,1)
    public static Expression p11 = new Expression.LetExpression(
            new Name("error"),
            new Expression.IntConstant(-1),
            new Expression.FunctionDeclExpression(
                    new Name("safeDivision"),
                    new Name[]{ new Name("dividend"), new Name("divisor")},
                    new Expression.IfExpression(
                            new Expression.EqExpression(
                                    new Expression.VariableExpression(new Name("divisor")),
                                    new Expression.IntConstant(0)
                            ),
                            new Expression.VariableExpression(new Name("error")),
                            new Expression.BinOpExpression(
                                    Expression.BinOpExpression.Operator.DIV,
                                    new Expression.VariableExpression(new Name("dividend")),
                                    new Expression.VariableExpression(new Name("divisor"))
                            )
                    ),
                    new Expression.LetExpression(
                            new Name("error"),
                            new Expression.IntConstant(-474),
                            new Expression.BinOpExpression(
                                    Expression.BinOpExpression.Operator.PLUS,
                                    new Expression.FunctionCallExpression(
                                            new Name("safeDivision"),
                                            new Expression[]{
                                                    new Expression.BinOpExpression(
                                                            Expression.BinOpExpression.Operator.PLUS,
                                                            new Expression.IntConstant(400),
                                                            new Expression.IntConstant(74)
                                                    ),
                                                    new Expression.IntConstant(0)
                                            }
                                    ),
                                    new Expression.FunctionCallExpression(
                                            new Name("safeDivision"),
                                            new Expression[]{
                                                    new Expression.IntConstant(474),
                                                    new Expression.IntConstant(1)
                                            }
                                    )
                            )
                    )
            )
    );


    // function fact(n) -> ((n == 1) ? 1 : n * fact(n-1))
    //   fact(10)
    public static Expression p12 = new Expression.FunctionDeclExpression(
            new Name("fact"),
            new Name[] { new Name("n")},
            new Expression.IfExpression(
                    new Expression.EqExpression(
                            new Expression.VariableExpression(new Name("n")),
                            new Expression.IntConstant(1)
                    ),
                    new Expression.IntConstant(1),
                    new Expression.BinOpExpression(
                            Expression.BinOpExpression.Operator.TIMES,
                            new Expression.VariableExpression(new Name("n")),
                            new Expression.FunctionCallExpression(
                                    new Name("fact"),
                                    new Expression[]{
                                            new Expression.BinOpExpression(
                                                    Expression.BinOpExpression.Operator.MINUS,
                                                    new Expression.VariableExpression(new Name("n")),
                                                    new Expression.IntConstant(1)
                                            )
                                    }
                            )
                    )
            ),
            new Expression.FunctionCallExpression(
                    new Name("fact"),
                    new Expression[] { new Expression.IntConstant(10) }
            )
    );

    // let error = -1 in
    //   let safeDivision = function safeDivision(dividend, divisor) -> (divisor == 0) ? error : dividend / divisor)) in
    //      let error = -474 in
    //        safeDivision(400 + 74,0) + safeDivision(474,1)
    public static Expression p13 = new Expression.LetExpression(
            new Name("error"),
            new Expression.IntConstant(-1),
            new Expression.LetExpression(
                    new Name("safeDivision"),
                    new Expression.FirstClassFunctionDeclExpression(
                            new Name("safeDivision"),
                            new Name[]{ new Name("dividend"), new Name("divisor")},
                            new Expression.IfExpression(
                                    new Expression.EqExpression(
                                            new Expression.VariableExpression(new Name("divisor")),
                                            new Expression.IntConstant(0)
                                    ),
                                    new Expression.VariableExpression(new Name("error")),
                                    new Expression.BinOpExpression(
                                            Expression.BinOpExpression.Operator.DIV,
                                            new Expression.VariableExpression(new Name("dividend")),
                                            new Expression.VariableExpression(new Name("divisor"))
                                    )
                            )
                    ),
                    new Expression.LetExpression(
                            new Name("error"),
                            new Expression.IntConstant(-474),
                            new Expression.BinOpExpression(
                                    Expression.BinOpExpression.Operator.PLUS,
                                    new Expression.FirstClassFunctionCallExpression(
                                            new Expression.VariableExpression(new Name("safeDivision")),
                                            new Expression[]{
                                                    new Expression.BinOpExpression(
                                                            Expression.BinOpExpression.Operator.PLUS,
                                                            new Expression.IntConstant(400),
                                                            new Expression.IntConstant(74)
                                                    ),
                                                    new Expression.IntConstant(0)
                                            }
                                    ),
                                    new Expression.FirstClassFunctionCallExpression(
                                            new Expression.VariableExpression(new Name("safeDivision")),
                                            new Expression[]{
                                                    new Expression.IntConstant(474),
                                                    new Expression.IntConstant(1)
                                            }
                                    )
                            )
                    )
            )
    );


    // let fact = function fact(n) -> ((n == 1) ? 1 : n * fact(n-1))
    //   fact(10)
    public static Expression p14 = new Expression.LetExpression(
            new Name("fact"),
            new Expression.FirstClassFunctionDeclExpression(
                    new Name("fact"),
                    new Name[] { new Name("n")},
                    new Expression.IfExpression(
                            new Expression.EqExpression(
                                    new Expression.VariableExpression(new Name("n")),
                                    new Expression.IntConstant(1)
                            ),
                            new Expression.IntConstant(1),
                            new Expression.BinOpExpression(
                                    Expression.BinOpExpression.Operator.TIMES,
                                    new Expression.VariableExpression(new Name("n")),
                                    new Expression.FirstClassFunctionCallExpression(
                                            new Expression.VariableExpression(new Name("fact")),
                                            new Expression[]{
                                                    new Expression.BinOpExpression(
                                                            Expression.BinOpExpression.Operator.MINUS,
                                                            new Expression.VariableExpression(new Name("n")),
                                                            new Expression.IntConstant(1)
                                                    )
                                            }
                                    )
                            )
                    )
            ),
            new Expression.FirstClassFunctionCallExpression(
                    new Expression.VariableExpression(new Name("fact")),
                    new Expression[] { new Expression.IntConstant(10) }
            )
    );

    //   let safeDivisionGenerator = (error) -> function safeDivision(dividend, divisor) -> (divisor == 0) ? error : dividend / divisor)) in
    //     let safeDivision-1 = safeDivisionGenerator(-1) in
    //       let safeDivision+1 = safeDivisionGenerator(1) in
    //         safeDivision-1(474,0) + safeDivision+1(474,0)
    public static Expression p15 = new Expression.LetExpression(
            new Name("safeDivisionGenerator"),
            new Expression.LambdaDeclExpression(
                    new Name[] { new Name("error") },
                    new Expression.FirstClassFunctionDeclExpression(
                            new Name("safeDivision"),
                            new Name[]{ new Name("dividend"), new Name("divisor")},
                            new Expression.IfExpression(
                                    new Expression.EqExpression(
                                            new Expression.VariableExpression(new Name("divisor")),
                                            new Expression.IntConstant(0)
                                    ),
                                    new Expression.VariableExpression(new Name("error")),
                                    new Expression.BinOpExpression(
                                            Expression.BinOpExpression.Operator.DIV,
                                            new Expression.VariableExpression(new Name("dividend")),
                                            new Expression.VariableExpression(new Name("divisor"))
                                    )
                            )
                    )),
            new Expression.LetExpression(
                    new Name("safeDivision-1"),
                    new Expression.FirstClassFunctionCallExpression(
                            new Expression.VariableExpression(new Name("safeDivisionGenerator")),
                            new Expression[] {new Expression.IntConstant(-1)}
                    ),
                    new Expression.LetExpression(
                            new Name("safeDivision+1"),
                            new Expression.FirstClassFunctionCallExpression(
                                    new Expression.VariableExpression(new Name("safeDivisionGenerator")),
                                    new Expression[] {new Expression.IntConstant(1)}
                            ),
                            new Expression.BinOpExpression(
                                    Expression.BinOpExpression.Operator.PLUS,
                                    new Expression.FirstClassFunctionCallExpression(
                                            new Expression.VariableExpression(new Name("safeDivision-1")),
                                            new Expression[] { new Expression.IntConstant(474), new Expression.IntConstant(0) }
                                    ),
                                    new Expression.FirstClassFunctionCallExpression(
                                            new Expression.VariableExpression(new Name("safeDivision+1")),
                                            new Expression[] { new Expression.IntConstant(474), new Expression.IntConstant(0) }
                                    )
                            )
                    )
            )
    );


    public static void main(String[] args) {
//        Map<String, Value> environemnt = new HashMap<>();
        Environment environment = Environment.EMPTY;
        System.out.println(new Interpreter().eval(p15, environment, new HashMap<>(), Assignment4.getSolution()));
    }
}
