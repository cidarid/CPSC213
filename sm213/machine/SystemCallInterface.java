package machine;

/**
 * Created by brx on 7/22/19.
 */

public interface SystemCallInterface {
    /* XXX exception inherits RuntimeException to avoid editing checked exception lists everywhere */
    class SyscallInterrupt extends RuntimeException {
        public int nr;
        public int[] args;
        public SyscallInterrupt(int nr, int[] args) {
            this.nr = nr;
            this.args = args;
        }
    }

    byte[] handleReadSyscall(int fd, int size);
    int handleWriteSyscall(int fd, byte[] data);
    int handleExecSyscall(byte[] command);
}
