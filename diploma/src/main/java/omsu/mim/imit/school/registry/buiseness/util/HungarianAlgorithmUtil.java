package omsu.mim.imit.school.registry.buiseness.util;

public class HungarianAlgorithmUtil {

    public static int[] kuhnMunkres(int[][] a) {
        int N = a[0].length;
        if (N == 0)
            return new int[0];

        long[] lx = new long[N], ly = new long[N];
        int[] mx = new int[N], my = new int[N];
        int[] px = new int[N], py = new int[N];
        int[] stack = new int[N];

        for (int i = 0; i < N; i++) {
            lx[i] = a[i][0];
            for (int j = 0; j < N; j++)
                if (a[i][j] > lx[i]) lx[i] = a[i][j];
            ly[i] = 0;
            mx[i] = my[i] = -1;
        }
        for (int size = 0; size < N; ) {
            int s;
            for (s = 0; mx[s] != -1; s++) ;
            for (int i = 0; i < N; i++)
                px[i] = py[i] = -1;
            px[s] = s;
            // DFS
            int t = -1;
            stack[0] = s;
            for (int top = 1; top > 0; ) {
                int u = stack[--top];
                for (int v = 0; v < N; v++) {
                    if (lx[u] + ly[v] == a[u][v] && py[v] == -1) {
                        if (my[v] == -1) {
                            t = v;
                            py[t] = u;
                            top = 0;
                            break;
                        }
                        py[v] = u;
                        px[my[v]] = v;
                        stack[top++] = my[v];
                    }
                }
            }
            if (t != -1) {
                while (true) {
                    int u = py[t];
                    mx[u] = t;
                    my[t] = u;
                    if (u == s) break;
                    t = px[u];
                }
                ++size;
            } else {
                Long delta = Long.MAX_VALUE;
                for (int u = 0; u < N; u++) {
                    if (px[u] == -1) continue;
                    for (int v = 0; v < N; v++) {
                        if (py[v] != -1) continue;
                        long z = lx[u] + ly[v] - a[u][v];
                        if (z < delta)
                            delta = z;
                    }
                }

                for (int i = 0; i < N; i++) {
                    if (px[i] != -1) lx[i] -= delta;
                    if (py[i] != -1) ly[i] += delta;
                }
            }
        }
        boolean correct = true;
        for (int u = 0; u < N; u++) {
            for (int v = 0; v < N; v++) {
                correct &= (lx[u] + ly[v] >= a[u][v]);
                if (mx[u] == v)
                    correct &= (lx[u] + ly[v] == a[u][v]);
                if (!correct) {
                    System.out.println("Assignment problem is not solved to optimality.");
                    for (int i = 0; i < mx.length; ++i)
                        mx[i] = i;
                }
            }
        }
        return mx;
    }
}
