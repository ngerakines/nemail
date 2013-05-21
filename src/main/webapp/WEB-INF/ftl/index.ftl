<#import "include.ftl" as layout>
<@layout.basic title="Hello">
<div class="row">
	<div class="span12">
		<h1>To</h1>
		<form class="form-horizontal" method="get">
			<div class="control-group">
				<label class="control-label" for="inputTo">To</label>
				<div class="controls">
					<input type="text" id="inputTo" name="to" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn">Find</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="row">
	<div class="span12">
		<h1>From</h1>
		<form class="form-horizontal" method="get">
			<div class="control-group">
				<label class="control-label" for="inputFrom">From</label>
				<div class="controls">
					<input type="text" id="inputFrom" name="from" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn">Find</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="row">
    <div class="span12">
        <h1>To</h1>
        <form class="form-horizontal" method="get">
            <div class="control-group">
                <label class="control-label" for="inputSubject">Subject</label>
                <div class="controls">
                    <input type="text" id="inputSubject" name="subject" />
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    <button type="submit" class="btn">Find</button>
                </div>
            </div>
        </form>
    </div>
</div>
<div class="row">
	<div class="span12">
		<ul>
			<#list emails as email>
				<li>Received on ${email.getReceivedAt().toString()} from ${email.getFrom()} for <#list email.getTos() as to>[${to}]</#list> regarding ${email.getSubject()}: <a href="/view/?messageId=${email.getMessageId()?url}">View</a></li>
			</#list>
		</ul>
	</div>
</div>
</@layout.basic>