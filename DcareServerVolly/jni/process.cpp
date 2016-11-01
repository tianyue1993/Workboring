#include "process.h"

extern ProcessBase *g_process;

extern const char* g_userId;

extern JNIEnv* g_env;

//�ӽ�����Ȩ�޷��ʸ����̵�˽��Ŀ¼,�ڴ˽��������ͨ�ŵ��׽����ļ�
static const char* PATH = "/data/data/com.etcomm.dcare/my.sock";

//��������
static const char* SERVICE_NAME =
		"com.etcomm.dcare/com.etcomm.dcare.service.DcareService";
bool ProcessBase::create_channel() {
}

int ProcessBase::write_to_channel(void* data, int len) {
	return write(m_channel, data, len);
}

int ProcessBase::read_from_channel(void* data, int len) {
	return read(m_channel, data, len);
}

int ProcessBase::get_channel() const {
	return m_channel;
}

void ProcessBase::set_channel(int channel_fd) {
	m_channel = channel_fd;
}

ProcessBase::ProcessBase() {

}

ProcessBase::~ProcessBase() {
	close(m_channel);
}

Parent::Parent(JNIEnv *env, jobject jobj) :
		m_env(env) {
	LOGE("<<new parent instance>>");

	m_jobj = env->NewGlobalRef(jobj);
}

Parent::~Parent() {
	LOGE("<<Parent::~Parent()>>");

	g_process = NULL;
}

void Parent::do_work() {
}

JNIEnv* Parent::get_jni_env() const {
	return m_env;
}

jobject Parent::get_jobj() const {
	return m_jobj;
}

/**
 * �����̴���ͨ��,������ʵ�Ǵ���һ���ͻ��˲�����
 * ���ӷ�����(�ӽ���)
 */
bool Parent::create_channel() {
	int sockfd;

	sockaddr_un addr;

	while (1) {
		sockfd = socket(AF_LOCAL, SOCK_STREAM, 0);

		if (sockfd < 0) {
			LOGE("<<Parent create channel failed>>");

			return false;
		}

		memset(&addr, 0, sizeof(addr));

		addr.sun_family = AF_LOCAL;

		strcpy(addr.sun_path, PATH);

		if (connect(sockfd, (sockaddr*) &addr, sizeof(addr)) < 0) {
			close(sockfd);

			sleep(1);

			continue;
		}

		set_channel(sockfd);

		LOGE("<<parent channel fd %d>>", m_channel);

		break;
	}

	return true;
}

/**
 * �ӽ��������ᷢ��SIGCHLD�ź�,ͨ����׽���źŸ����̿���
 * ֪���ӽ����Ѿ�����,�˺�����ΪSIGCHLD�źŵĴ�����.
 */
static void sig_handler(int signo) {
	pid_t pid;

	int status;

//����wait�ȴ��ӽ�������ʱ������SIGCHLD
//�ź��Ը��ӽ�����ʬ����ֹ����ɽ�ʬ����
	pid = wait(&status);

	if (g_process != NULL) {
		g_process->on_child_end();
	}
}

void Parent::catch_child_dead_signal() {
	LOGE("<<process %d install child dead signal detector!>>", getpid());

	struct sigaction sa;

	sigemptyset(&sa.sa_mask);

	sa.sa_flags = 0;

	sa.sa_handler = sig_handler;

	sigaction(SIGCHLD, &sa, NULL);
}

void Parent::on_child_end() {
	LOGE("<<on_child_end:create a new child process>>");

	create_child();
}

bool Parent::create_child() {
	pid_t pid;

	if ((pid = fork()) < 0) {
		return false;
	} else if (pid == 0) //�ӽ���
			{
		LOGE("<<In child process,pid=%d>>", getpid());

		Child child;

		ProcessBase& ref_child = child;

		ref_child.do_work();
	} else if (pid > 0)  //������
			{
		LOGE("<<In parent process,pid=%d>>", getpid());
	}

	return true;
}

bool Child::create_child() {
//�ӽ��̲���Ҫ��ȥ�����ӽ���,�˺�������
	return false;
}

Child::Child() {
	RTN_MAP.member_rtn = &Child::parent_monitor;
}

Child::~Child() {
	LOGE("<<~Child(), unlink %s>>", PATH);

	unlink (PATH);
}

void Child::catch_child_dead_signal() {
//�ӽ��̲���Ҫ��׽SIGCHLD�ź�
	return;
}

void Child::on_child_end() {
//�ӽ��̲���Ҫ����
	return;
}

void Child::handle_parent_die() {
//�ӽ��̳�Ϊ�˹¶�����,�ȴ���Init�����������ڽ��к�������
	while (getppid() != 1) {
		usleep(500); //����0.5ms
	}

	close (m_channel);

//���������̷���
	LOGE("<<parent died,restart now>>");

	restart_parent();
}

void Child::restart_parent() {
	LOGE("<<restart_parent enter>>");

	/**
	 * TODO ����������,ͨ��am����Java�ռ����һ���(service����activity��)������Ӧ����������
	 */
	execlp("am", "am", "startservice", "--user", g_userId, "-n", SERVICE_NAME, //ע��˴�������
			(char *) NULL);
}

void* Child::parent_monitor() {
	handle_parent_die();
}

void Child::start_parent_monitor() {
	pthread_t tid;

	pthread_create(&tid, NULL, RTN_MAP.thread_rtn, this);
}

bool Child::create_channel() {
	int listenfd, connfd;

	struct sockaddr_un addr;

	listenfd = socket(AF_LOCAL, SOCK_STREAM, 0);

	unlink (PATH);

	memset(&addr, 0, sizeof(addr));

	addr.sun_family = AF_LOCAL;

	strcpy(addr.sun_path, PATH);

	if (bind(listenfd, (sockaddr*) &addr, sizeof(addr)) < 0) {
		LOGE("<<bind error,errno(%d)>>", errno);

		return false;
	}

	listen(listenfd, 5);

	while (true) {
		if ((connfd = accept(listenfd, NULL, NULL)) < 0) {
			if (errno == EINTR)
				continue;
			else {
				LOGE("<<accept error>>");

				return false;
			}
		}

		set_channel(connfd);

		break;
	}

	LOGE("<<child channel fd %d>>", m_channel);

	return true;
}

void Child::handle_msg(const char* msg) {
//TODO How to handle message is decided by you.
}

void Child::listen_msg() {
	fd_set rfds;

	int retry = 0;

	while (1) {
		FD_ZERO(&rfds);

		FD_SET(m_channel, &rfds);

		timeval timeout = { 3, 0 };

		int r = select(m_channel + 1, &rfds, NULL, NULL, &timeout);

		if (r > 0) {
			char pkg[256] = { 0 };

			if (FD_ISSET(m_channel, &rfds)) {
				read_from_channel(pkg, sizeof(pkg));

				LOGE("<<A message comes:%s>>", pkg);

				handle_msg((const char*) pkg);
			}
		}
	}
}

void Child::do_work() {
	start_parent_monitor(); //���������߳�

	if (create_channel())  //�ȴ����Ҵ������Ը����̷��͵���Ϣ
	{
		listen_msg();
	}
}

