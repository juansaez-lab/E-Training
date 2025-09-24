export default class ConstUrls {

    public static readonly PROTOCOL = 'http';
    public static readonly HOST = 'localhost';
    public static readonly PORT = ':8080';

    public static readonly BASE_URL = this.PROTOCOL + '://' + this.HOST + this.PORT; 
    public static readonly API_URL = this.BASE_URL + '/e-Training';

    public static readonly CONTEXT_LOGIN = '/login';
    public static readonly CONTEXT_USER = '/user';
    public static readonly CONTEXT_QUESTION = '/question';
    public static readonly CONTEXT_TESTEXECUTION = '/testexecution';
    public static readonly CONTEXT_TEST_EXECUTION = '/testexecution';
    public static readonly CONTEXT_TEST ='/tests';

    public static readonly NICK_USUARIO_PARAM = 'nickUsuario';
    public static readonly PASS_USUARIO_PARAM = 'contrasena';

    public static readonly CONTEXT_SUBJECT = '/subject';  
    
}
